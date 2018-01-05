package cn.com.guardiantech.aofgo.backend.service

import cn.com.guardiantech.aofgo.backend.data.entity.Account
import cn.com.guardiantech.aofgo.backend.data.entity.Student
import cn.com.guardiantech.aofgo.backend.repo.AccountRepo
import cn.com.guardiantech.aofgo.backend.repo.StudentRepo
import cn.com.guardiantech.aofgo.backend.request.student.StudentCreationRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StudentService @Autowired constructor(
        val studentRepo: StudentRepo,
        val accountRepo: AccountRepo
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
}