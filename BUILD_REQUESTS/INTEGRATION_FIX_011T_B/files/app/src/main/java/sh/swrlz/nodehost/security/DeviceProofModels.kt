package sh.swrlz.nodehost.security

enum class ProofBindingState {
    BOUND,
    ENROLLMENT_REQUIRED,
    REVOKED,
    UNAVAILABLE,
    CONFLICT,
}

enum class ProofBindingSource {
    NEW_REGISTRATION,
    LEGACY_ENROLLMENT,
    REBIND,
}

data class ProofBindingRecord(
    val bindingVersion: Int = 1,
    val deviceId: String,
    val serverInstallationId: String,
    val state: ProofBindingState,
    val ciphertextBase64: String?,
    val nonceBase64: String?,
    val createdAtEpochMs: Long,
    val updatedAtEpochMs: Long,
    val keyFingerprintSha256: String?,
    val source: ProofBindingSource?,
)

data class ProofBindingLookup(
    val state: ProofBindingState,
    val proofKey: ByteArray? = null,
    val errorCode: String? = null,
)
