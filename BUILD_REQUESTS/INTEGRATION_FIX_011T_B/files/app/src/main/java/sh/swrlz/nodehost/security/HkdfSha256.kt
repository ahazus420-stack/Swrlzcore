package sh.swrlz.nodehost.security

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object HkdfSha256 {
    private const val HMAC_ALGORITHM = "HmacSHA256"
    private const val OUTPUT_LENGTH = 32
    private const val INFO = "SWRLZ-DEVICE-PROOF-KEY-V1"
    private const val SALT_PREFIX = "SWRLZ-DEVICE-PROOF-SALT-V1\n"

    fun deriveProofKey(rawDeviceKey: CharArray, serverInstallationId: String, deviceId: String): ByteArray {
        require(serverInstallationId.isNotBlank())
        require(deviceId.isNotBlank())
        val ikm = String(rawDeviceKey).toByteArray(StandardCharsets.UTF_8)
        val saltInput = "$SALT_PREFIX$serverInstallationId\n$deviceId".toByteArray(StandardCharsets.UTF_8)
        val salt = MessageDigest.getInstance("SHA-256").digest(saltInput)
        return try { expand(extract(salt, ikm), INFO.toByteArray(StandardCharsets.UTF_8), OUTPUT_LENGTH) }
        finally { ikm.fill(0); saltInput.fill(0); salt.fill(0) }
    }

    private fun extract(salt: ByteArray, ikm: ByteArray): ByteArray = Mac.getInstance(HMAC_ALGORITHM).run {
        init(SecretKeySpec(salt, HMAC_ALGORITHM)); doFinal(ikm)
    }

    private fun expand(prk: ByteArray, info: ByteArray, length: Int): ByteArray = try {
        val output = ByteArray(length)
        var previous = ByteArray(0)
        var offset = 0
        var counter = 1
        while (offset < length) {
            val block = Mac.getInstance(HMAC_ALGORITHM).run {
                init(SecretKeySpec(prk, HMAC_ALGORITHM)); update(previous); update(info); update(counter.toByte()); doFinal()
            }
            previous.fill(0); previous = block
            val count = minOf(block.size, length - offset)
            block.copyInto(output, offset, 0, count)
            offset += count; counter++
        }
        previous.fill(0); output
    } finally { prk.fill(0) }
}
