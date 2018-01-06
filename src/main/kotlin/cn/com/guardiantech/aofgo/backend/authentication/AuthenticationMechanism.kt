package cn.com.guardiantech.aofgo.backend.authentication

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Credential
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.CredentialType
import org.apache.commons.codec.binary.Hex
import org.springframework.stereotype.Component
import java.security.MessageDigest

@Component
class AuthenticationMechanism {

    private val sha256Digest = MessageDigest.getInstance("SHA-256")

    private fun computeSHA256String(raw: String) = String(Hex.encodeHex(sha256Digest.digest(raw.toByteArray())))

    fun encryptCredentialSecret(type: CredentialType, raw: String): String = when (type) {
        CredentialType.PASSWORD -> {
            computeSHA256String(raw)
        }
        else -> raw
    }

    fun verifyCredentialSecret(credential: Credential, attempt: String): Boolean = when (credential.type) {

        CredentialType.PASSWORD -> {
            credential.secret == computeSHA256String(attempt)
        }

        else -> false
    }

}