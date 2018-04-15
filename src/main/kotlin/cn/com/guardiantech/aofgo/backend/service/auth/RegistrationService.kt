package cn.com.guardiantech.aofgo.backend.service.auth

import cn.com.guardiantech.aofgo.backend.data.entity.AccountType
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.PrincipalType
import cn.com.guardiantech.aofgo.backend.request.authentication.EmailAddressSubmissionRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.registraion.EmailValidationResponse
import cn.com.guardiantech.aofgo.backend.request.authentication.registraion.EmailValidationResult
import cn.com.guardiantech.aofgo.backend.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody

@Service
class RegistrationService @Autowired constructor(
        private val authenticationService: AuthenticationService,
        private val accountService: AccountService
) {
    fun checkEmailAddressValidity(email: String): EmailValidationResult {
        // Check If a subject already exists
        val validationResult =
                if (authenticationService.subjectExists(PrincipalType.EMAIL, email)) {
                    EmailValidationResult.ACCOUNT_EXIST
                } else {
                    val account = accountService.getAccountByEmail(email = email)
                    when (account?.type) {
                        AccountType.STUDENT -> EmailValidationResult.STUDENT_RECORD
                        AccountType.FACULTY -> EmailValidationResult.FACULTY_RECORD
                        AccountType.PARENT -> EmailValidationResult.PARENT_RECORD
                        else -> EmailValidationResult.NONE
                    }
                }
        return validationResult
    }
}