package sh.swrlz.nodehost.security

import android.content.Context
import android.util.AtomicFile
import android.util.Base64
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class ProofBindingSidecar(
    context: Context,
    private val serverInstallationId: String,
    private val cipher: AndroidKeystoreProofCipher = AndroidKeystoreProofCipher(),
) {
    private val file = AtomicFile(File(context.filesDir, "device-proof-bindings-v1.json"))
    private val lock = ReentrantReadWriteLock()

    fun lookup(deviceId: String): ProofBindingLookup = lock.read {
        val matches = load().filter { it.bindingVersion == 1 && it.serverInstallationId == serverInstallationId && it.deviceId == deviceId }
        if (matches.size > 1) return@read ProofBindingLookup(ProofBindingState.CONFLICT, errorCode = "PROOF_BINDING_CONFLICT")
        val record = matches.singleOrNull()
            ?: return@read ProofBindingLookup(ProofBindingState.ENROLLMENT_REQUIRED, errorCode = "PROOF_ENROLLMENT_REQUIRED")
        if (record.state != ProofBindingState.BOUND) return@read ProofBindingLookup(record.state, errorCode = code(record.state))
        return@read try {
            val key = cipher.decrypt(
                Base64.decode(record.ciphertextBase64, Base64.NO_WRAP),
                Base64.decode(record.nonceBase64, Base64.NO_WRAP),
                aad(record),
            )
            if (!MessageDigest.isEqual(sha256Hex(key).toByteArray(), record.keyFingerprintSha256.orEmpty().toByteArray())) {
                key.fill(0); ProofBindingLookup(ProofBindingState.CONFLICT, errorCode = "PROOF_BINDING_CONFLICT")
            } else ProofBindingLookup(ProofBindingState.BOUND, proofKey = key)
        } catch (_: Exception) {
            ProofBindingLookup(ProofBindingState.UNAVAILABLE, errorCode = "PROOF_KEY_UNAVAILABLE")
        }
    }

    fun bind(deviceId: String, proofKey: ByteArray, source: ProofBindingSource, now: Long): ProofBindingRecord = lock.write {
        val records = load().toMutableList()
        val matches = records.filter { it.bindingVersion == 1 && it.serverInstallationId == serverInstallationId && it.deviceId == deviceId }
        require(matches.size <= 1) { "PROOF_BINDING_CONFLICT" }
        matches.singleOrNull()?.let {
            if (it.state == ProofBindingState.BOUND && it.keyFingerprintSha256 == sha256Hex(proofKey)) return@write it
            throw IllegalStateException("PROOF_BINDING_CONFLICT")
        }
        val template = ProofBindingRecord(1, deviceId, serverInstallationId, ProofBindingState.BOUND, null, null, now, now, sha256Hex(proofKey), source)
        val encrypted = cipher.encrypt(proofKey, aad(template))
        val bound = template.copy(
            ciphertextBase64 = Base64.encodeToString(encrypted.ciphertext, Base64.NO_WRAP),
            nonceBase64 = Base64.encodeToString(encrypted.nonce, Base64.NO_WRAP),
        )
        records += bound; persist(records); bound
    }

    private fun load(): List<ProofBindingRecord> {
        if (!file.baseFile.exists()) return emptyList()
        return try {
            file.openRead().bufferedReader(StandardCharsets.UTF_8).use { reader ->
                val root = JSONObject(reader.readText())
                require(root.getInt("sidecarVersion") == 1)
                val array = root.getJSONArray("bindings")
                buildList { for (i in 0 until array.length()) add(decode(array.getJSONObject(i))) }
            }
        } catch (e: Exception) { throw ProofKeyUnavailableException("PROOF_STORAGE_CORRUPT", e) }
    }

    private fun persist(records: List<ProofBindingRecord>) {
        val root = JSONObject().put("sidecarVersion", 1).put("bindings", JSONArray().apply {
            records.sortedWith(compareBy({ it.serverInstallationId }, { it.deviceId }, { it.bindingVersion })).forEach { put(encode(it)) }
        })
        val bytes = root.toString().toByteArray(StandardCharsets.UTF_8)
        val out = file.startWrite()
        try { out.write(bytes); out.fd.sync(); file.finishWrite(out) } catch (e: Exception) { file.failWrite(out); throw e } finally { bytes.fill(0) }
    }

    private fun aad(r: ProofBindingRecord) = "${r.bindingVersion}\n${r.serverInstallationId}\n${r.deviceId}\n${r.state.name}".toByteArray()
    private fun sha256Hex(v: ByteArray) = MessageDigest.getInstance("SHA-256").digest(v).joinToString("") { "%02x".format(it) }
    private fun code(s: ProofBindingState) = when (s) {
        ProofBindingState.BOUND -> "INTERNAL_ERROR"; ProofBindingState.ENROLLMENT_REQUIRED -> "PROOF_ENROLLMENT_REQUIRED"
        ProofBindingState.REVOKED -> "PROOF_KEY_REVOKED"; ProofBindingState.UNAVAILABLE -> "PROOF_KEY_UNAVAILABLE"
        ProofBindingState.CONFLICT -> "PROOF_BINDING_CONFLICT"
    }
    private fun encode(r: ProofBindingRecord) = JSONObject().put("bindingVersion", r.bindingVersion).put("deviceId", r.deviceId)
        .put("serverInstallationId", r.serverInstallationId).put("state", r.state.name).put("ciphertext", r.ciphertextBase64)
        .put("nonce", r.nonceBase64).put("createdAt", r.createdAtEpochMs).put("updatedAt", r.updatedAtEpochMs)
        .put("keyFingerprint", r.keyFingerprintSha256).put("source", r.source?.name)
    private fun decode(j: JSONObject) = ProofBindingRecord(j.getInt("bindingVersion"), j.getString("deviceId"), j.getString("serverInstallationId"),
        ProofBindingState.valueOf(j.getString("state")), j.optString("ciphertext").takeIf { it.isNotBlank() && it != "null" },
        j.optString("nonce").takeIf { it.isNotBlank() && it != "null" }, j.getLong("createdAt"), j.getLong("updatedAt"),
        j.optString("keyFingerprint").takeIf { it.isNotBlank() && it != "null" },
        j.optString("source").takeIf { it.isNotBlank() && it != "null" }?.let(ProofBindingSource::valueOf))
}
