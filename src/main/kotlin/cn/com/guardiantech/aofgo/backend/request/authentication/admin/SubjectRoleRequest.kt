package cn.com.guardiantech.aofgo.backend.request.authentication.admin

import cn.com.guardiantech.aofgo.backend.annotation.NoArg

@NoArg
class SubjectRoleRequest(
        val subjectId: Long,
        val role: String?,
        val roles: Set<String>?
)