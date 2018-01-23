package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.annotation.Require
import cn.com.guardiantech.aofgo.backend.data.entity.Student
import cn.com.guardiantech.aofgo.backend.exception.BadRequestException
import cn.com.guardiantech.aofgo.backend.exception.ControllerException
import cn.com.guardiantech.aofgo.backend.exception.EntityNotFoundException
import cn.com.guardiantech.aofgo.backend.exception.RepositoryException
import cn.com.guardiantech.aofgo.backend.request.student.StudentEditCardSecretRequest
import cn.com.guardiantech.aofgo.backend.request.student.StudentRequest
import cn.com.guardiantech.aofgo.backend.request.student.StudentCreationWithNewAccountRequest
import cn.com.guardiantech.aofgo.backend.service.StudentService
import javassist.NotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/student")
class StudentController @Autowired constructor(
        val studentService: StudentService
) {
    private val logger: Logger = LoggerFactory.getLogger(StudentController::class.java)

    @Require
    @PutMapping("/")
    fun createStudent(@RequestBody @Valid studentRequest: StudentRequest): Student = try {
        studentService.createStudent(studentRequest)
    } catch (e: NoSuchElementException) {
        throw EntityNotFoundException("Account Not Found")
    } catch (e: IllegalArgumentException) {
        throw BadRequestException(e.message)
    } catch (e: Throwable) {
        logger.error("Student Saving Error:", e)
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
    @PostMapping("/")
    fun editStudent(@RequestBody @Valid request: StudentRequest) = try {
        if (request.idNumber == null) throw BadRequestException("You no send idNumber!")
        studentService.editStudent(request)
    } catch (e: ControllerException) {
        throw e
    } catch (e: NoSuchElementException) {
        throw EntityNotFoundException("Student Not Found")
    } catch (e: Throwable) {
        logger.error("Failed while saving student", e)
        throw RepositoryException("Failed to save student")
    }

    @Require
    @DeleteMapping("/card/{idNumber}")
    fun unbindStudentCard(@PathVariable idNumber: String): Student = try {
        studentService.editStudentCardSecret(idNumber, null)
    } catch (e: NoSuchElementException) {
        throw EntityNotFoundException("Student Not Found")
    } catch (e: Throwable) {
        throw RepositoryException("Failed to save student")
    }

    @Require
    @PutMapping("/card")
    fun editStudentCard(@RequestBody @Valid request: StudentEditCardSecretRequest): Student = try {
        studentService.editStudentCardSecret(request.idNumber, request.cardSecret)
    } catch (e: NoSuchElementException) {
        throw EntityNotFoundException("Student Not Found")
    } catch (e: Throwable) {
        throw RepositoryException("Failed to save student")
    }

}