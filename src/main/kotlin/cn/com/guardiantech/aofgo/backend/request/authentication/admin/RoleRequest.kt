package cn.com.guardiantech.aofgo.backend.request.authentication.admin

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@NoArg
class RoleRequest (
        @field:NotNull
        @field:Size(min=1)
        val roleName: String,
        val permissions: Set<String>? = null
)