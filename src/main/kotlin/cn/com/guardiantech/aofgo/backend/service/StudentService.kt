package cn.com.guardiantech.aofgo.backend.service

import cn.com.guardiantech.aofgo.backend.data.entity.Account
import cn.com.guardiantech.aofgo.backend.data.entity.Student
import cn.com.guardiantech.aofgo.backend.repository.AccountRepository
import cn.com.guardiantech.aofgo.backend.repository.StudentRepository
import cn.com.guardiantech.aofgo.backend.request.student.StudentCreationRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class StudentService @Autowired constructor(
        private val studentRepo: StudentRepository,
        private val accountRepo: AccountRepository
) {

    fun createStudent(studentCreationRequest: StudentCreationRequest): Student {
        var theAccount: Account? = null
        if (studentCreationRequest.accountId != null) {
            // NoSuchElementException: Invalid Account
            theAccount = accountRepo.findById(studentCreationRequest.accountId).get()
        }
        return studentRepo.save(Student(
                idNumber = studentCreationRequest.idNumber,
                cardSecret = studentCreationRequest.cardSecret,
                grade = studentCreationRequest.grade,
                dateOfBirth = studentCreationRequest.dateOfBirth,
                gender = studentCreationRequest.gender,
                dorm = studentCreationRequest.dorm,
                dormInfo = studentCreationRequest.dormInfo,
                account = theAccount
        ))
    }

    fun listAllStudents(): List<Student> {
        return studentRepo.findAll().toList()
    }
}