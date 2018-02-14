package cn.com.guardiantech.aofgo.backend.data.entity.authentication

/**
 * Created by Codetector on 30/11/2017.
 * Project aofgo-backend
 */
enum class CredentialType(val weight: Short) {
    PASSWORD(0b100000000000000),
    TOTP(0b000000000000001)
}