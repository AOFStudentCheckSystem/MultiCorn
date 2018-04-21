package cn.com.guardiantech.aofgo.backend.request.authentication

import cn.com.guardiantech.aofgo.backend.annotation.NoArg

@NoArg
class VeracrossInitialAuthRequest(
        val username: String,
        val password: String
        )