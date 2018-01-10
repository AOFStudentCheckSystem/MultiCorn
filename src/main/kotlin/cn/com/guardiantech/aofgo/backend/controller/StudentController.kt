package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.annotation.Require
import cn.com.guardiantech.aofgo.backend.data.entity.Student
import cn.com.guardiantech.aofgo.backend.exception.RepositoryException
import cn.com.guardiantech.aofgo.backend.request.student.StudentCreationRequest
import cn.com.guardiantech.aofgo.backend.service.StudentService
import javassist.NotFoundException
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
            throw NotFoundException("Invalid Account")
        } catch (e: Throwable) {
            throw RepositoryException("Failed to save student due to conflict")
        }
    }

    @Require
    @GetMapping("/listall")
    fun listAllStudent(): List<Student> {
        return studentService.listAllStudents()
    }

    @Require
    @GetMapping("/{id}")
    fun getStudentByIdNumber(@PathVariable id: String): Student = try {
        studentService.getStudentByIdNumber(id)
    } catch (e: NoSuchElementException) {
        throw NotFoundException("Student Not Found")
    }
}