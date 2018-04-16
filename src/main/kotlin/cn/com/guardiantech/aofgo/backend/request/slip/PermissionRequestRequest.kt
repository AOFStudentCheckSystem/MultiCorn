package cn.com.guardiantech.aofgo.backend.request.slip

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import javax.validation.constraints.NotNull

/**
 * Created by dedztbh on 18-3-2.
 * Project AOFGoBackend
 */
@NoArg
class PermissionRequestRequest(
        @field:NotNull
        val id: Long,
        @field:NotNull
        val accepted: Boolean
)