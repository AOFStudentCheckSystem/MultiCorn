package cn.com.guardiantech.aofgo.backend.service

import cn.com.guardiantech.aofgo.backend.data.entity.Account
import cn.com.guardiantech.aofgo.backend.data.entity.AccountType
import cn.com.guardiantech.aofgo.backend.data.entity.Student
import cn.com.guardiantech.aofgo.backend.exception.EntityNotFoundException
import cn.com.guardiantech.aofgo.backend.exception.RepositoryException
import cn.com.guardiantech.aofgo.backend.repository.StudentRepository
import cn.com.guardiantech.aofgo.backend.request.student.StudentRequest
import cn.com.guardiantech.aofgo.backend.request.student.StudentCreationWithNewAccountRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class StudentService @Autowired constructor(
        private val studentRepo: StudentRepository,
        private val accountService: AccountService
) {
    /**
     * Failed to save student due to conflict
     */
    @Transactional
    fun createStudent(request: StudentRequest): Student {
        val theAccount: Account = if (request.accountId != null) {
            accountService.getAccountById(request.accountId)
        } else {
            if (request.account != null) {
                request.account.type = AccountType.STUDENT
                accountService.createAccount(request.account)
            } else {
                throw IllegalArgumentException("No account is provided with the student.")
            }
        }

        return studentRepo.save(Student(
                idNumber = request.idNumber,
                cardSecret = request.cardSecret,
                grade = request.grade,
                dateOfBirth = request.dateOfBirth,
                gender = request.gender,
                dorm = request.dorm,
                dormInfo = request.dormInfo,
                account = theAccount
        ))
    }

    fun listAllStudents(): List<Student> {
        return studentRepo.findAll().toList()
    }

    /**
     * @throws NoSuchElementException Student Not Found
     */
    fun getStudentByIdNumber(id: String): Student {
        return studentRepo.findByIdNumber(id).get()
    }

    /**
     * @throws NoSuchElementException Student Not Found
     * Failed to save student
     */
    fun editStudent(request: StudentRequest): Student {
        val theStudent = studentRepo.findByIdNumber(request.idNumber).get()
        if (request.cardSecret != null) {
            theStudent.cardSecret = request.cardSecret
        }
        if (request.grade != null) {
            theStudent.grade = request.grade
        }
        if (request.gender != null) {
            theStudent.gender = request.gender
        }
        if (request.dateOfBirth != null) {
            theStudent.dateOfBirth = request.dateOfBirth
        }
        if (request.dorm != null) {
            theStudent.dorm = request.dorm
        }
        if (request.dormInfo != null) {
            theStudent.dormInfo = request.dormInfo
        }
        if (request.accountId != null && request.accountId != theStudent.account?.id) {
            theStudent.account = try {
                accountService.getAccountById(request.accountId)
            } catch (e: NoSuchElementException) {
                throw EntityNotFoundException("Account Not Found")
            }
        }
        return studentRepo.save(theStudent)
    }
}