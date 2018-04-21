package cn.com.guardiantech.aofgo.backend.service.auth

import cn.com.guardiantech.aofgo.backend.data.entity.Account
import cn.com.guardiantech.aofgo.backend.data.entity.AccountType
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.CredentialType
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.PrincipalType
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Subject
import cn.com.guardiantech.aofgo.backend.data.entity.email.EmailTemplateTypeEnum
import cn.com.guardiantech.aofgo.backend.exception.BadRequestException
import cn.com.guardiantech.aofgo.backend.exception.EntityNotFoundException
import cn.com.guardiantech.aofgo.backend.repository.auth.AccountRepository
import cn.com.guardiantech.aofgo.backend.repository.auth.PrincipalRepository
import cn.com.guardiantech.aofgo.backend.request.authentication.CredentialRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.PrincipalRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.SubjectRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.registraion.EmailValidationResult
import cn.com.guardiantech.aofgo.backend.request.authentication.registraion.UsernameValidationResult
import cn.com.guardiantech.aofgo.backend.request.authentication.registraion.RegistrationRequest
import cn.com.guardiantech.aofgo.backend.service.AccountService
import cn.com.guardiantech.aofgo.backend.service.EmailVerificationService
import cn.com.guardiantech.aofgo.backend.service.StudentService
import cn.com.guardiantech.aofgo.backend.service.email.EmailService
import cn.com.guardiantech.aofgo.backend.service.email.EmailTemplatingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Year
import java.util.*

@Service
class RegistrationService @Autowired constructor(
        private val authenticationService: AuthenticationService,
        private val accountService: AccountService,
        private val accountRepository: AccountRepository,
        private val studentService: StudentService,
        private val emailVerificationService: EmailVerificationService,
        private val emailService: EmailService,
        private val emailTemplatingService: EmailTemplatingService,
        private val principalRepository: PrincipalRepository
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

    fun checkUsernameValidity(username: String): UsernameValidationResult {
        // Verify Username conforms to the regex:

        if(username.matches(Regex("[A-z][0-9A-z\\-_.]*"))) {
            val find = principalRepository.findByTypeAndIdentification(PrincipalType.USERNAME, username)
            if (find.isPresent) {
                return UsernameValidationResult.OCCUPIED
            } else {
                return UsernameValidationResult.AVAILABLE
            }
        }
        return UsernameValidationResult.INVALID
    }

    @Transactional
    fun registerUserWithEmail(registerRequest: RegistrationRequest) {
        if (authenticationService.subjectExists(PrincipalType.EMAIL, registerRequest.email)) throw BadRequestException("Subject Already Exists")
        val emailValidity = checkEmailAddressValidity(registerRequest.email)
        val newSubject = authenticationService.registerSubject(
                SubjectRequest(
                        principal = PrincipalRequest(
                                type = PrincipalType.EMAIL,
                                identification = registerRequest.email
                        ),
                        credential = CredentialRequest(
                                type = CredentialType.PASSWORD,
                                secret = registerRequest.password
                        )
                )
        )
        val account = when (emailValidity) {
            EmailValidationResult.STUDENT_RECORD -> {
                val optStudent = studentService.findStudentByAccountEmail(registerRequest.email)
                if (!optStudent.isPresent) throw EntityNotFoundException("Student Not Found")
                // If account is null, findStudentByAccountEmail would not have found it
                optStudent.get().let {
                    if (it.account!!.subject !== null) throw BadRequestException("Account occupied")
                    it.account!!.subject = newSubject
                    accountRepository.save(it.account)
                }!!
            }
            EmailValidationResult.FACULTY_RECORD -> {
                registerNonStudent(registerRequest.email, newSubject)
            }
            EmailValidationResult.PARENT_RECORD -> {
                registerNonStudent(registerRequest.email, newSubject)
            }
        //TODO: User registration without existing account with email
            else -> {
                throw BadRequestException("Cannot create account because email status is: $emailValidity")
            }
        }
        val validationCode = emailVerificationService.assignEmailValidationCode(account)
        emailService.defaultTemplate[EmailTemplateTypeEnum.REGVERIFY]!!.let {
            emailTemplatingService.compileTemplate(
                    it,
                    HashMap<String, Any>().apply {
                        put("firstName", account.firstName)
                        put("lastName", account.lastName)
                        put("link", validationCode.code)
                    }
            ).let {
                emailService.sendEmail(it.first, it.second, account.email!!)
            }
        }
    }

    @Transactional
    private fun registerNonStudent(email: String, newSubject: Subject): Account {
        val account = accountService.getAccountByEmail(email) ?: throw BadRequestException("Account Not Found")
        if (account.subject !== null) throw BadRequestException("Account occupied")
        account.subject = newSubject
        return accountRepository.save(account)
    }
}