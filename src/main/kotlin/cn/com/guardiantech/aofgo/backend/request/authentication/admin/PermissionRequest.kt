package cn.com.guardiantech.aofgo.backend.request.authentication.admin

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotNull
import kotlin.math.min

@NoArg
class PermissionRequest(
        @NotNull
        @Length(min = 1)
        val permissionKey: String
)