package cn.com.guardiantech.aofgo.backend.request.authentication.admin

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import javax.validation.constraints.NotNull

@NoArg
class RoleRequest (
        @NotNull
        val roleName: String,
        val permissions: Set<String>? = null
){
}