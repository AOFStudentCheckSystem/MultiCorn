package cn.com.guardiantech.aofgo.backend.request.account

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import cn.com.guardiantech.aofgo.backend.data.entity.AccountType
import cn.com.guardiantech.aofgo.backend.request.authentication.SubjectRequest

@NoArg
data class AccountRequest(
        val firstName: String,
        val lastName: String,
        val email: String?,
        val phone: String?,
        var type: AccountType?,
        val preferredName: String,
        val subjectId: Long?,
        val subject: SubjectRequest?,
        val id: Long?
)