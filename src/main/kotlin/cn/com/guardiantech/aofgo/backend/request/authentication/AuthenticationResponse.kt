package cn.com.guardiantech.aofgo.backend.request.authentication

import cn.com.guardiantech.aofgo.backend.annotation.NoArg

@NoArg
data class AuthenticationResponse(
        val session: String
)