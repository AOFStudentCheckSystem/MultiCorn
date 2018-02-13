package cn.com.guardiantech.aofgo.backend.request.authentication.admin

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotNull

@NoArg
class PermissionRequest(
        @field:NotNull
        @field:Length(min = 1)
        val permissionKey: String
)