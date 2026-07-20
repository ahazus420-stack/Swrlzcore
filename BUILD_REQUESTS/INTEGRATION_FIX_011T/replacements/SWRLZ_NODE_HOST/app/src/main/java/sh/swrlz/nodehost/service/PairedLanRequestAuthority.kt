package sh.swrlz.nodehost.service

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.time.Instant
import java.time.format.DateTimeParseException
import java.util.Locale
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Bounded authorization rules for the paired local-LAN bootstrap lane.
 *
 * Pairing authorizes an attempt against this SERVER. A device proof separately
 * proves possession of the stable key already bound to a known registry record.
 * This class does not grant membership, administrator, mission, or trust-root
 * authority and does not mutate the presence registry.
 */
internal class PairedLanRequestAuthority(
    private val pairingValidator: (String?) -> Boolean,
    private val proofKeyResolver: DeviceProofKeyResolver = DeviceProofKeyResolver.UNBOUND,
    private val nowEpochMs: () -> Long = System::currentTimeMillis,
    private val allowedClockSkewMs: Long = DEFAULT_CLOCK_SKEW_MS,
    private val replayWindowMs: Long = DEFAULT_REPLAY_WINDOW_MS,
) {
    private val replayLock = Any()
    private val acceptedNonces = linkedMapOf<String, Long>()

    fun authorizeResolveTransport(request: DiscoveryHttpRequest): String {
        if (request.duplicateHeaders.any(SECURITY_CRITICAL_HEADERS::contains)) {
            throw PairedLanAuthorizationFailure(400, "MALFORMED_REQUEST", "Duplicate security headers are not allowed")
        }
        val requestId = request.headers[REQUEST_ID_HEADER]
            ?.takeIf(::validRequestComponent)
            ?: throw PairedLanAuthorizationFailure(
                httpStatus = 400,
                code = "REQUEST_ID_REQUIRED",
                safeMessage = "x-swrlz-request-id is required",
            )

        val pairingToken = if (request.isLoopback) {
            request.headers[PAIRING_HEADER] ?: request.headers[LEGACY_PAIRING_HEADER]
        } else {
            if (!request.isLocalLan) {
                throw PairedLanAuthorizationFailure(
                    httpStatus = 403,
                    code = "LOCAL_NETWORK_REQUIRED",
                    safeMessage = "A verified local-LAN path is required",
                )
            }
            request.headers[PAIRING_HEADER]
        }
        if (pairingToken.isNullOrBlank()) {
            throw PairedLanAuthorizationFailure(401, "PAIRING_REQUIRED", "Pairing authorization is required")
        }
        if (!pairingValidator(pairingToken)) {
            throw PairedLanAuthorizationFailure(401, "PAIRING_INVALID", "Pairing authorization was rejected")
        }
        return requestId
    }

    /**
     * Validates a known-device HMAC proof. A missing bound proof key is reported
     * distinctly so the route can return ACTION_REQUIRED without weakening proof.
     */
    fun verifyKnownDevice(
        request: DiscoveryHttpRequest,
        expectedDeviceId: String,
        protocolVersion: Int,
        schemaVersion: Int,
    ): DeviceProofVerification {
        val requestId = request.headers[REQUEST_ID_HEADER]
            ?.takeIf(::validRequestComponent)
            ?: throw PairedLanAuthorizationFailure(400, "REQUEST_ID_REQUIRED", "x-swrlz-request-id is required")
        val headerDeviceId = request.headers[DEVICE_ID_HEADER]
            ?.takeIf(::validDeviceId)
            ?: throw PairedLanAuthorizationFailure(401, "DEVICE_PROOF_REQUIRED", "Device proof headers are required")
        if (headerDeviceId != expectedDeviceId) {
            throw PairedLanAuthorizationFailure(409, "IDENTITY_CONFLICT", "Device proof identity does not match request identity")
        }
        val timestampText = request.headers[DEVICE_TIMESTAMP_HEADER]
            ?.takeIf(::validRequestComponent)
            ?: throw PairedLanAuthorizationFailure(401, "DEVICE_PROOF_REQUIRED", "Device proof timestamp is required")
        val nonce = request.headers[DEVICE_NONCE_HEADER]
            ?.takeIf(::validNonce)
            ?: throw PairedLanAuthorizationFailure(401, "DEVICE_PROOF_REQUIRED", "Device proof nonce is required")
        val suppliedProof = request.headers[DEVICE_PROOF_HEADER]
            ?.trim()
            ?.lowercase(Locale.US)
            ?.takeIf { LOWER_HEX_256.matches(it) }
            ?: throw PairedLanAuthorizationFailure(401, "DEVICE_PROOF_REQUIRED", "Device proof is required")

        val timestampMs = parseTimestamp(timestampText)
            ?: throw PairedLanAuthorizationFailure(401, "DEVICE_PROOF_STALE", "Device proof timestamp is invalid")
        val now = nowEpochMs()
        if (timestampMs < now - allowedClockSkewMs || timestampMs > now + allowedClockSkewMs) {
            throw PairedLanAuthorizationFailure(401, "DEVICE_PROOF_STALE", "Device proof timestamp is outside the accepted window")
        }

        val resolvedKey = proofKeyResolver.resolve(expectedDeviceId)
            ?: return DeviceProofVerification.KeyUnbound
        val key = resolvedKey.copyOf()
        try {
            val bodyHash = request.bodySha256Hex
                .takeIf { LOWER_HEX_256.matches(it) }
                ?: throw PairedLanAuthorizationFailure(400, "MALFORMED_REQUEST", "Request body digest is invalid")
            val canonical = listOf(
                request.method,
                request.path,
                protocolVersion.toString(),
                schemaVersion.toString(),
                expectedDeviceId,
                timestampText,
                nonce,
                requestId,
                bodyHash,
            ).joinToString("\n")
            val expectedProof = hmacSha256Hex(key, canonical)
            if (!MessageDigest.isEqual(
                    expectedProof.toByteArray(StandardCharsets.US_ASCII),
                    suppliedProof.toByteArray(StandardCharsets.US_ASCII),
                )
            ) {
                throw PairedLanAuthorizationFailure(401, "DEVICE_PROOF_INVALID", "Device proof was rejected")
            }
            acceptNonce(expectedDeviceId, nonce, now)
            return DeviceProofVerification.Verified(
                deviceKeyFingerprint = sha256Hex(key),
            )
        } finally {
            key.fill(0)
        }
    }

    private fun acceptNonce(deviceId: String, nonce: String, now: Long) = synchronized(replayLock) {
        purgeReplayEntries(now)
        val replayKey = "$deviceId\u0000$nonce"
        if (acceptedNonces.containsKey(replayKey)) {
            throw PairedLanAuthorizationFailure(409, "DEVICE_PROOF_REPLAYED", "Device proof nonce was already accepted")
        }
        acceptedNonces[replayKey] = now
        while (acceptedNonces.size > MAX_REPLAY_ENTRIES) {
            val oldest = acceptedNonces.entries.firstOrNull()?.key ?: break
            acceptedNonces.remove(oldest)
        }
    }

    private fun purgeReplayEntries(now: Long) {
        val cutoff = now - replayWindowMs
        val iterator = acceptedNonces.entries.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().value < cutoff) iterator.remove()
        }
    }

    private fun parseTimestamp(value: String): Long? {
        value.toLongOrNull()?.let { return it }
        return try {
            Instant.parse(value).toEpochMilli()
        } catch (_: DateTimeParseException) {
            null
        }
    }


    private fun hmacSha256Hex(key: ByteArray, value: String): String {
        val mac = Mac.getInstance(HMAC_SHA256)
        mac.init(SecretKeySpec(key, HMAC_SHA256))
        return hex(mac.doFinal(value.toByteArray(StandardCharsets.UTF_8)))
    }

    private fun sha256Hex(value: ByteArray): String = hex(MessageDigest.getInstance("SHA-256").digest(value))

    private fun hex(value: ByteArray): String = buildString(value.size * 2) {
        value.forEach { byte -> append("%02x".format(Locale.US, byte.toInt() and 0xff)) }
    }

    private fun validDeviceId(value: String): Boolean = value.length in 1..128 &&
        value.all { it.code in 0x21..0x7E }

    private fun validRequestComponent(value: String): Boolean = value.length in 1..128 &&
        value.all { it.code in 0x21..0x7E }

    private fun validNonce(value: String): Boolean = value.length in 16..192 &&
        value.all { it.code in 0x21..0x7E }

    companion object {
        const val PAIRING_HEADER = "x-swrlz-pairing-token"
        const val LEGACY_PAIRING_HEADER = "x-swurlz-pairing-token"
        const val REQUEST_ID_HEADER = "x-swrlz-request-id"
        const val DEVICE_ID_HEADER = "x-swrlz-device-id"
        const val DEVICE_TIMESTAMP_HEADER = "x-swrlz-device-timestamp"
        const val DEVICE_NONCE_HEADER = "x-swrlz-device-nonce"
        const val DEVICE_PROOF_HEADER = "x-swrlz-device-proof"

        const val DEFAULT_CLOCK_SKEW_MS = 120_000L
        const val DEFAULT_REPLAY_WINDOW_MS = 300_000L
        private const val MAX_REPLAY_ENTRIES = 4_096
        private const val HMAC_SHA256 = "HmacSHA256"
        private val LOWER_HEX_256 = Regex("[a-f0-9]{64}")
        private val SECURITY_CRITICAL_HEADERS = setOf(
            PAIRING_HEADER,
            LEGACY_PAIRING_HEADER,
            REQUEST_ID_HEADER,
            DEVICE_ID_HEADER,
            DEVICE_TIMESTAMP_HEADER,
            DEVICE_NONCE_HEADER,
            DEVICE_PROOF_HEADER,
        )
    }
}

internal fun interface DeviceProofKeyResolver {
    /** Returns a fresh copy of the raw symmetric device proof key, or null when not bound. */
    fun resolve(deviceId: String): ByteArray?

    companion object {
        val UNBOUND = DeviceProofKeyResolver { null }
    }
}

internal sealed interface DeviceProofVerification {
    data class Verified(val deviceKeyFingerprint: String) : DeviceProofVerification
    object KeyUnbound : DeviceProofVerification
}

internal class PairedLanAuthorizationFailure(
    val httpStatus: Int,
    val code: String,
    val safeMessage: String,
    val retryable: Boolean = false,
) : Exception(safeMessage)
