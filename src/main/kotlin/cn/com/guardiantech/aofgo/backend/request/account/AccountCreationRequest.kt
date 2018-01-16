package cn.com.guardiantech.aofgo.backend.request.account

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import cn.com.guardiantech.aofgo.backend.data.entity.AccountType

@NoArg
data class AccountCreationRequest(
        val firstName: String,
        val lastName: String,
        val email: String,
        val phone: String?,
        val type: AccountType,
        val preferredName: String,
        val subjectId: Long?
)