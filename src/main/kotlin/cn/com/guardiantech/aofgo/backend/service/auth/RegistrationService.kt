package cn.com.guardiantech.aofgo.backend.service.auth

import cn.com.guardiantech.aofgo.backend.data.entity.AccountType
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.PrincipalType
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Subject
import cn.com.guardiantech.aofgo.backend.exception.BadRequestException
import cn.com.guardiantech.aofgo.backend.exception.EntityNotFoundException
import cn.com.guardiantech.aofgo.backend.repository.auth.AccountRepository
import cn.com.guardiantech.aofgo.backend.request.authentication.SubjectRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.registraion.EmailValidationResult
import cn.com.guardiantech.aofgo.backend.service.AccountService
import cn.com.guardiantech.aofgo.backend.service.StudentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegistrationService @Autowired constructor(
        private val authenticationService: AuthenticationService,
        private val accountService: AccountService,
        private val accountRepository: AccountRepository,
        private val studentService: StudentService
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

    @Transactional
    fun bindingRegisterSubject(subjectRequest: SubjectRequest): Subject {
        if (authenticationService.subjectExists(subjectRequest.principal.type, subjectRequest.principal.identification)) throw BadRequestException("Subject Already Exists")
        val emailValidity = checkEmailAddressValidity(subjectRequest.principal.identification)
        val newSubject = authenticationService.registerSubject(subjectRequest)
        when (emailValidity) {
            EmailValidationResult.STUDENT_RECORD -> {
                val optStudent = studentService.findStudentByAccountEmail(subjectRequest.principal.identification)
                if (!optStudent.isPresent) throw EntityNotFoundException("Student Not Found")
                // If account is null, findStudentByAccountEmail would not have found it
                return optStudent.get().let {
                    it.account!!.subject = newSubject
                    accountRepository.save(it.account)
                }!!.subject!!
            }
            EmailValidationResult.FACULTY_RECORD -> {
                return registerNonStudent(subjectRequest.principal.identification, newSubject)
            }
            EmailValidationResult.PARENT_RECORD -> {
                return registerNonStudent(subjectRequest.principal.identification, newSubject)
            }
            else -> {
                throw BadRequestException("Cannot create account because email status is: $emailValidity")
            }
        }
    }

    private fun registerNonStudent(email: String, newSubject: Subject): Subject {
        val account = accountService.getAccountByEmail(email) ?: throw EntityNotFoundException("Account not found")
        account.subject = newSubject
        return accountRepository.save(account).subject!!
    }
}