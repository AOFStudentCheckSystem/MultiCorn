package cn.com.guardiantech.aofgo.backend.request.authentication

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.CredentialType

@NoArg
data class CredentialRequest(
        val type: CredentialType,
        var secret: String
)