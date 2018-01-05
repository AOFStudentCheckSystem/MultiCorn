package cn.com.guardiantech.aofgo.backend.request.authentication

import cn.com.guardiantech.aofgo.backend.annotation.NoArg

@NoArg
data class RegisterRequest (
    val principal: PrincipalRequest,
    val credential: CredentialRequest,
    val subjectAttachedInfo: String
)
