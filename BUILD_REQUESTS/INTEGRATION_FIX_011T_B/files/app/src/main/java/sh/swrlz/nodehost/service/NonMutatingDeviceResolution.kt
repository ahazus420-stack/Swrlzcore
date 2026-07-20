package sh.swrlz.nodehost.service

import sh.swrlz.nodehost.security.ProofBindingSidecar
import sh.swrlz.nodehost.security.ProofBindingState
import sh.swrlz.nodehost.security.ProofRequest
import sh.swrlz.nodehost.security.RequestProofVerifier

enum class PresenceStatus { ACTIVE, RETIRED, REPLACED, BLOCKED }

data class PresenceDevice(val deviceId: String, val status: PresenceStatus, val lineageDeviceId: String? = null)

interface ReadOnlyPresenceRegistry { fun findExactDevice(deviceId: String): PresenceDevice? }

data class DeviceResolveRequest(val protocolVersion: Int, val schemaVersion: Int, val proof: ProofRequest)

data class DeviceResolveResponse(
    val protocolVersion: Int = 1,
    val schemaVersion: Int = 1,
    val result: String,
    val errorCode: String? = null,
    val deviceStatus: String? = null,
    val lineageDeviceId: String? = null,
)

class NonMutatingDeviceResolution(
    private val registry: ReadOnlyPresenceRegistry,
    private val sidecar: ProofBindingSidecar,
    private val verifier: RequestProofVerifier,
) {
    fun resolve(request: DeviceResolveRequest): DeviceResolveResponse {
        if (request.protocolVersion != 1 || request.schemaVersion != 1) {
            return DeviceResolveResponse(result = "INCOMPATIBLE", errorCode = "PROTOCOL_SCHEMA_UNSUPPORTED")
        }
        val existing = registry.findExactDevice(request.proof.deviceId)
            ?: return DeviceResolveResponse(result = "UNKNOWN")
        if (existing.status != PresenceStatus.ACTIVE) {
            return DeviceResolveResponse(
                result = "KNOWN_NONACTIVE",
                deviceStatus = existing.status.name,
                lineageDeviceId = existing.lineageDeviceId,
            )
        }
        val binding = sidecar.lookup(existing.deviceId)
        if (binding.state == ProofBindingState.ENROLLMENT_REQUIRED) {
            return DeviceResolveResponse(result = "ACTION_REQUIRED", errorCode = "PROOF_ENROLLMENT_REQUIRED")
        }
        if (binding.state != ProofBindingState.BOUND || binding.proofKey == null) {
            return DeviceResolveResponse(result = "DENIED", errorCode = binding.errorCode ?: "PROOF_KEY_UNAVAILABLE")
        }
        return try {
            val checked = verifier.verify(request.proof, binding.proofKey)
            if (checked.accepted) DeviceResolveResponse(result = "KNOWN", deviceStatus = existing.status.name)
            else DeviceResolveResponse(result = "DENIED", errorCode = checked.errorCode)
        } finally {
            binding.proofKey.fill(0)
        }
    }
}
