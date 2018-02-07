package cn.com.guardiantech.aofgo.backend.request.authentication.admin

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import javax.validation.constraints.NotNull

/**
 * Created by dedztbh on 18-2-4.
 * Project AOFGoBackend
 */
@NoArg
class SubjectEditRequest(
        @field:NotNull
        val id: Long,
        val subjectAttachedInfo: String?,
        val roles: Set<String>?
)