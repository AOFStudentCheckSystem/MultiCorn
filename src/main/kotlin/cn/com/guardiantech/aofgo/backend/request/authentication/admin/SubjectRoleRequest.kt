package cn.com.guardiantech.aofgo.backend.request.authentication.admin

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import javax.validation.constraints.NotNull

@NoArg
class SubjectRoleRequest(
        @field:NotNull
        val subjectId: Long,
        val role: String?,
        val roles: Set<String>?
)