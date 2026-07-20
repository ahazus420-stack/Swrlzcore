package sh.swrlz.nodehost.security

import java.net.InetAddress
import java.security.MessageDigest

interface PairingTokenStore { fun activeTokenHashSha256(): ByteArray? }

data class LanAuthorizationRequest(
    val remoteAddress: InetAddress,
    val localAddress: InetAddress,
    val suppliedPairingToken: CharArray,
)

data class LanAuthorizationResult(val authorized: Boolean, val errorCode: String? = null)

class PairedLanAuthorizer(private val tokenStore: PairingTokenStore) {
    fun authorize(request: LanAuthorizationRequest): LanAuthorizationResult {
        if (request.remoteAddress.isAnyLocalAddress || request.remoteAddress.isMulticastAddress) {
            return LanAuthorizationResult(false, "LAN_CALLER_NOT_ALLOWED")
        }
        if (!sameSubnetConservative(request.remoteAddress.address, request.localAddress.address)) {
            return LanAuthorizationResult(false, "LAN_CALLER_OUTSIDE_LOCAL_SUBNET")
        }
        val expected = tokenStore.activeTokenHashSha256() ?: return LanAuthorizationResult(false, "PAIRING_REQUIRED")
        val supplied = String(request.suppliedPairingToken).toByteArray(Charsets.UTF_8)
        return try {
            val actual = MessageDigest.getInstance("SHA-256").digest(supplied)
            if (MessageDigest.isEqual(expected, actual)) LanAuthorizationResult(true)
            else LanAuthorizationResult(false, "PAIRING_INVALID")
        } finally {
            supplied.fill(0); request.suppliedPairingToken.fill('\u0000')
        }
    }

    private fun sameSubnetConservative(remote: ByteArray, local: ByteArray): Boolean {
        if (remote.size != local.size) return false
        return when (remote.size) {
            4 -> remote[0] == local[0] && remote[1] == local[1] && remote[2] == local[2]
            16 -> (0 until 8).all { remote[it] == local[it] }
            else -> false
        }
    }
}
