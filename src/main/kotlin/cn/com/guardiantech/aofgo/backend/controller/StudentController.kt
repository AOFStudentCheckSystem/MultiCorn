package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.annotation.Require
import cn.com.guardiantech.aofgo.backend.data.entity.Student
import cn.com.guardiantech.aofgo.backend.request.student.StudentCreationRequest
import cn.com.guardiantech.aofgo.backend.service.StudentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/student")
class StudentController @Autowired constructor(
        val studentService: StudentService
) {
    @Require
    @PutMapping("/")
    fun createStudent(@RequestBody studentCreationRequest: StudentCreationRequest): Student {
        try {
            return studentService.createStudent(studentCreationRequest)
        } catch (e: NoSuchElementException) {
            throw e
        } catch (e: Throwable) {
            throw e
        }
    }

    @Require
    @GetMapping("/listall")
    fun listAllStudent(): List<Student> {
        return studentService.listAllStudents()
    }
}