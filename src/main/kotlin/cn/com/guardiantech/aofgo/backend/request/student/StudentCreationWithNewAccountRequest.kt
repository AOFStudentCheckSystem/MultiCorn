package cn.com.guardiantech.aofgo.backend.request.student

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import cn.com.guardiantech.aofgo.backend.request.account.AccountCreationRequest

@NoArg
data class StudentCreationWithNewAccountRequest(
        val student: StudentCreationRequest,
        val account: AccountCreationRequest
)