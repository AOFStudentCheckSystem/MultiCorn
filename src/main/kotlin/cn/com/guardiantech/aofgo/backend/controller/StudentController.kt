package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.annotation.Require
import cn.com.guardiantech.aofgo.backend.data.entity.Student
import cn.com.guardiantech.aofgo.backend.exception.ControllerException
import cn.com.guardiantech.aofgo.backend.exception.RepositoryException
import cn.com.guardiantech.aofgo.backend.request.student.StudentRequest
import cn.com.guardiantech.aofgo.backend.request.student.StudentCreationWithNewAccountRequest
import cn.com.guardiantech.aofgo.backend.service.AccountService
import cn.com.guardiantech.aofgo.backend.service.StudentService
import com.fasterxml.jackson.databind.ObjectMapper
import javassist.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/student")
class StudentController @Autowired constructor(
        val studentService: StudentService
) {
    @Require
    @PutMapping("/create/")
    fun createStudent(@RequestBody @Valid studentRequest: StudentRequest): Student = try {
        studentService.createStudent(studentRequest)
    } catch (e: NoSuchElementException) {
        throw NotFoundException("Account Not Found")
    } catch (e: Throwable) {
        throw RepositoryException("Failed to save student due to conflict")
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

    @Require
    @PutMapping("/account")
    fun createStudentWithNewAccount(@RequestBody @Valid request: StudentCreationWithNewAccountRequest): Student =
            try {
                studentService.createStudentWithNewAccount(request)
            } catch (e: ControllerException) {
                throw e
            } catch (e: Throwable) {
                throw RepositoryException("Failed to save student due to conflict")
            }

    @Require
    @PostMapping("/")
    fun editStudent(@RequestBody @Valid request: StudentRequest) = try {
        studentService.editStudent(request)
    } catch (e: ControllerException) {
        throw e
    } catch (e: NoSuchElementException) {
        throw NotFoundException("Student Not Found")
    } catch (e: Throwable) {
        throw RepositoryException("Failed to save student")
    }

}