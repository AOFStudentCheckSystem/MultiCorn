package cn.com.guardiantech.aofgo.backend.service

import cn.com.guardiantech.aofgo.backend.data.entity.Account
import cn.com.guardiantech.aofgo.backend.data.entity.Student
import cn.com.guardiantech.aofgo.backend.exception.RepositoryException
import cn.com.guardiantech.aofgo.backend.repository.StudentRepository
import cn.com.guardiantech.aofgo.backend.request.student.StudentCreationRequest
import cn.com.guardiantech.aofgo.backend.request.student.StudentCreationWithNewAccountRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StudentService @Autowired constructor(
        private val studentRepo: StudentRepository,
        private val accountService: AccountService
) {
    /**
     * Failed to save student due to conflict
     */
    fun createStudent(request: StudentCreationRequest, savedAccount: Account? = null): Student {
        var theAccount: Account? = null
        if (savedAccount != null) {
            theAccount = savedAccount
        } else if (request.accountId != null) {
            theAccount = accountService.getAccountById(request.accountId)
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

    fun createStudentWithNewAccount(request: StudentCreationWithNewAccountRequest): Student {
        return createStudent(request.student, try {
            accountService.createAccount(request.account)
        } catch (e: Throwable) {
            throw RepositoryException("Cannot save account")
        })
    }
}