package cn.com.guardiantech.aofgo.backend.service

import cn.com.guardiantech.aofgo.backend.data.entity.Account
import cn.com.guardiantech.aofgo.backend.data.entity.Student
import cn.com.guardiantech.aofgo.backend.request.student.StudentCreationRequest

class StudentService {

    fun createStudent(studentCreationRequest: StudentCreationRequest): Student {
//        return entityManager {
//            transactional(it) {
//                var theAccount: Account? = null
//                if (studentCreationRequest.accountId != null) {
//                    theAccount = it
//                            .createQuery("FROM Account A WHERE A.id = :id")
//                            .setParameter("id", studentCreationRequest.accountId)
//                            // NoResultException: Invalid Account
//                            .singleResult as Account
//                }
//                val newStudent = it.merge(Student(
//                        idNumber = studentCreationRequest.idNumber,
//                        cardSecret = studentCreationRequest.cardSecret,
//                        grade = studentCreationRequest.grade,
//                        dateOfBirth = studentCreationRequest.dateOfBirth,
//                        gender = studentCreationRequest.gender,
//                        dorm = studentCreationRequest.dorm,
//                        dormInfo = studentCreationRequest.dormInfo,
//                        account = theAccount
//                ))
//                it.flush()
//                it.refresh(newStudent)
//                newStudent
//            }
//        }
        TODO()
    }

}