package cn.com.guardiantech.aofgo.backend.util

import org.apache.commons.codec.binary.Base64
import java.security.SecureRandom

object SessionUtil {
    private val secureRandom = SecureRandom()

    fun secureRandomBase64Identifier(numberOfBytes: Int = 32) = with(ByteArray(numberOfBytes)) {
        secureRandom.nextBytes(this)
        Base64.encodeBase64String(this)!!
    }
}