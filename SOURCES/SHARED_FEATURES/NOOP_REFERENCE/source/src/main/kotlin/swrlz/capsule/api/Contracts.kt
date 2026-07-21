package swrlz.capsule.api

data class ServiceId(val value: String)

object ServiceIds {
    val AUDIT_SINK = ServiceId("swrlz.audit_sink.v1")
    val CLOCK = ServiceId("swrlz.clock.v1")
    val EPHEMERAL_STATE = ServiceId("swrlz.ephemeral_state.v1")
}

enum class CompatibilityCode {
    COMPATIBLE,
    CONTRACT_VERSION_UNSUPPORTED,
    RUNTIME_TARGET_UNSUPPORTED,
    REQUIRED_SERVICE_MISSING,
    SERVICE_VERSION_UNSUPPORTED,
    DESCRIPTOR_INVALID,
    SOURCE_CHECKSUM_MISMATCH,
    INITIALIZATION_CONFLICT,
    CAPSULE_QUARANTINED,
    INTEGRATION_MANIFEST_INVALID
}

data class CompatibilityResult(
    val compatible: Boolean,
    val code: CompatibilityCode,
    val detail: String
)

data class CapsuleDescriptor(
    val capsuleId: String,
    val capsuleVersion: String,
    val contractVersion: Int,
    val runtimeTargets: Set<String>,
    val requiredServices: Set<ServiceId>,
    val optionalServices: Set<ServiceId>,
    val permissions: Set<String> = emptySet(),
    val components: Set<String> = emptySet(),
    val networkRoutes: Set<String> = emptySet(),
    val storageNamespace: String,
    val storageSchemaVersion: Int,
    val failurePolicy: String,
    val truthFirewallImpact: String
)

interface AuditSink {
    fun record(event: AuditEvent)
}

data class AuditEvent(
    val capsuleId: String,
    val eventType: String,
    val reasonCode: String,
    val timestamp: Long
)

interface HostClock { fun now(): Long }
interface EphemeralState {
    fun put(namespace: String, key: String, value: String)
    fun get(namespace: String, key: String): String?
    fun clear(namespace: String)
}

data class HostServices(
    val runtimeTarget: String,
    val contractVersion: Int,
    val auditSink: AuditSink?,
    val clock: HostClock?,
    val ephemeralState: EphemeralState? = null,
    val advertisedServiceVersions: Map<ServiceId, Int> = emptyMap()
)

enum class LifecycleState { NEW, INITIALIZED, STARTED, STOPPED, QUARANTINED }

data class NoOpRequest(val requestId: String)
sealed interface NoOpResult {
    data class Executed(
        val capsuleId: String,
        val capsuleVersion: String,
        val hostTimestamp: Long,
        val executionState: String
    ) : NoOpResult
    data class Rejected(val reasonCode: CompatibilityCode, val detail: String) : NoOpResult
}
