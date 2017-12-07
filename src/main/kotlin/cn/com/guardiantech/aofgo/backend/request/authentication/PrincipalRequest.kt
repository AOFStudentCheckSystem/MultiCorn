package cn.com.guardiantech.aofgo.backend.request.authentication

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.PrincipalType

@NoArg
data class PrincipalRequest(
        val type: PrincipalType,

        val identification: String
)