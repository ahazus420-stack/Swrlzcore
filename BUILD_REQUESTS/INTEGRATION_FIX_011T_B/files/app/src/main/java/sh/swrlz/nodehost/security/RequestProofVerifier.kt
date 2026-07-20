package sh.swrlz.nodehost.security

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.time.Clock
import java.util.concurrent.ConcurrentHashMap
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

interface ReplayLedger {
    fun consume(deviceId: String, requestId: String, nonce: String, expiresAtEpochMs: Long): Boolean
}

class InMemoryReplayLedger(private val clock: Clock = Clock.systemUTC()) : ReplayLedger {
    private val entries = ConcurrentHashMap<String, Long>()
    override fun consume(deviceId: String, requestId: String, nonce: String, expiresAtEpochMs: Long): Boolean {
        val now = clock.millis()
        entries.entries.removeIf { it.value < now }
        return entries.putIfAbsent("$deviceId\u0000$requestId\u0000$nonce", expiresAtEpochMs) == null
    }
}

data class ProofRequest(
    val method: String,
    val route: String,
    val deviceId: String,
    val requestId: String,
    val timestampEpochMs: Long,
    val nonce: String,
    val body: ByteArray,
    val suppliedBodySha256Hex: String,
    val suppliedHmacHex: String,
)

data class ProofVerification(val accepted: Boolean, val errorCode: String? = null)

class RequestProofVerifier(
    private val replayLedger: ReplayLedger,
    private val clock: Clock = Clock.systemUTC(),
    private val allowedClockSkewMs: Long = 120_000,
) {
    fun verify(request: ProofRequest, proofKey: ByteArray): ProofVerification {
        if (request.requestId.isBlank() || request.nonce.isBlank()) return ProofVerification(false, "INVALID_PROOF_ENVELOPE")
        if (kotlin.math.abs(clock.millis() - request.timestampEpochMs) > allowedClockSkewMs) return ProofVerification(false, "PROOF_TIMESTAMP_OUT_OF_RANGE")
        val bodyHash = sha256Hex(request.body)
        if (!equalHex(bodyHash, request.suppliedBodySha256Hex)) return ProofVerification(false, "BODY_HASH_MISMATCH")
        val canonical = listOf(request.method.uppercase(), request.route, request.deviceId, request.requestId,
            request.timestampEpochMs.toString(), request.nonce, bodyHash).joinToString("\n").toByteArray(StandardCharsets.UTF_8)
        val expected = try {
            Mac.getInstance("HmacSHA256").run {
                init(SecretKeySpec(proofKey, "HmacSHA256")); doFinal(canonical).joinToString("") { "%02x".format(it) }
            }
        } finally { canonical.fill(0) }
        if (!equalHex(expected, request.suppliedHmacHex)) return ProofVerification(false, "DEVICE_PROOF_INVALID")
        val fresh = replayLedger.consume(request.deviceId, request.requestId, request.nonce, request.timestampEpochMs + allowedClockSkewMs)
        return if (fresh) ProofVerification(true) else ProofVerification(false, "PROOF_REPLAY_DETECTED")
    }

    private fun equalHex(a: String, b: String) = MessageDigest.isEqual(
        a.lowercase().toByteArray(StandardCharsets.US_ASCII), b.lowercase().toByteArray(StandardCharsets.US_ASCII))
    private fun sha256Hex(v: ByteArray) = MessageDigest.getInstance("SHA-256").digest(v).joinToString("") { "%02x".format(it) }
}
