package cn.com.guardiantech.aofgo.backend.request.authentication

import cn.codetector.jet.controller.annotation.NoArg

@NoArg
data class AuthenticationResponse(
        val session: String
)