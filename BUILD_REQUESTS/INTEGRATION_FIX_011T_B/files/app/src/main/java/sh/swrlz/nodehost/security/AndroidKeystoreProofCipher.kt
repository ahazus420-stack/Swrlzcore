package sh.swrlz.nodehost.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class AndroidKeystoreProofCipher(private val alias: String = "swrlz.device-proof.master.v1") {
    data class EncryptedPayload(val ciphertext: ByteArray, val nonce: ByteArray)
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

    fun encrypt(plaintext: ByteArray, associatedData: ByteArray): EncryptedPayload {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateMasterKey())
        cipher.updateAAD(associatedData)
        return EncryptedPayload(cipher.doFinal(plaintext), cipher.iv)
    }

    fun decrypt(ciphertext: ByteArray, nonce: ByteArray, associatedData: ByteArray): ByteArray {
        val key = keyStore.getKey(alias, null) as? SecretKey
            ?: throw ProofKeyUnavailableException("PROOF_KEYSTORE_INVALIDATED")
        return Cipher.getInstance("AES/GCM/NoPadding").run {
            init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, nonce)); updateAAD(associatedData); doFinal(ciphertext)
        }
    }

    private fun getOrCreateMasterKey(): SecretKey {
        (keyStore.getKey(alias, null) as? SecretKey)?.let { return it }
        val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        generator.init(
            KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .setRandomizedEncryptionRequired(true)
                .setUserAuthenticationRequired(false)
                .build()
        )
        return generator.generateKey()
    }
}

class ProofKeyUnavailableException(val code: String, cause: Throwable? = null) : RuntimeException(code, cause)
