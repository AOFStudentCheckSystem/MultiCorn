package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.annotation.Require
import cn.com.guardiantech.aofgo.backend.data.entity.Student
import cn.com.guardiantech.aofgo.backend.exception.BadRequestException
import cn.com.guardiantech.aofgo.backend.exception.ControllerException
import cn.com.guardiantech.aofgo.backend.exception.EntityNotFoundException
import cn.com.guardiantech.aofgo.backend.exception.RepositoryException
import cn.com.guardiantech.aofgo.backend.repository.StudentPagedRepository
import cn.com.guardiantech.aofgo.backend.request.student.ImportStudentRequest
import cn.com.guardiantech.aofgo.backend.request.student.StudentEditCardSecretRequest
import cn.com.guardiantech.aofgo.backend.request.student.StudentRequest
import cn.com.guardiantech.aofgo.backend.service.StudentService
import javassist.NotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import java.io.FileOutputStream
import java.io.BufferedOutputStream
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.io.File
import java.io.InputStream


@RestController
@RequestMapping("/student")
class StudentController @Autowired constructor(
        val studentService: StudentService,
        private val studentPagedRepository: StudentPagedRepository
) {
    private val logger: Logger = LoggerFactory.getLogger(StudentController::class.java)

    @Require(["STUDENT_WRITE"])
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

    @Require(["STUDENT_READ"])
    @GetMapping("/")
    fun listStudentPaged(pageable: Pageable): Page<Student> {
        return studentPagedRepository.findAll(pageable)
    }

    @Require(["STUDENT_READ"])
    @GetMapping("/listall")
    fun listAllStudent(): List<Student> {
        return studentService.listAllStudents()
    }

    @Require(["STUDENT_READ"])
    @GetMapping("/{id}")
    fun getStudentByIdNumber(@PathVariable id: String): Student = try {
        studentService.getStudentByIdNumber(id)
    } catch (e: NoSuchElementException) {
        throw NotFoundException("Student Not Found")
    }


    @Require(["STUDENT_WRITE"])
    @PostMapping("/")
    fun editStudent(@RequestBody @Valid request: StudentRequest) = try {
        studentService.editStudent(request)
    } catch (e: ControllerException) {
        throw e
    } catch (e: NoSuchElementException) {
        throw EntityNotFoundException("Student Not Found")
    } catch (e: Throwable) {
        logger.error("Failed while saving student", e)
        throw RepositoryException("Failed to save student")
    }

    @Require(["STUDENT_WRITE"])
    @DeleteMapping("/card/{idNumber}")
    fun unbindStudentCard(@PathVariable idNumber: String): Student = try {
        studentService.editStudentCardSecret(idNumber, null)
    } catch (e: NoSuchElementException) {
        throw EntityNotFoundException("Student Not Found")
    } catch (e: Throwable) {
        throw RepositoryException("Failed to save student")
    }

    @Require(["STUDENT_WRITE"])
    @PutMapping("/card")
    fun editStudentCard(@RequestBody @Valid request: StudentEditCardSecretRequest): Student = try {
        studentService.editStudentCardSecret(request.idNumber, request.cardSecret)
    } catch (e: NoSuchElementException) {
        throw EntityNotFoundException("Student Not Found")
    } catch (e: Throwable) {
        throw RepositoryException("Failed to save student")
    }

    //    @Require(["STUDENT_WRITE", "ACCOUNT_WRITE"])
//    @PostMapping("/import")
//    fun importStudents(@RequestBody request: ImportStudentRequest): Student = try {
//        studentService.importStudentsFromCsv(request.csvContent.toString())
//    } catch (e: Throwable) {
//        throw RepositoryException("Failed to import student")
//    }
//}
//    @Require(["STUDENT_WRITE", "ACCOUNT_WRITE"])
    @PostMapping("/import")
    @ResponseBody
    fun handleFileUpload(
            @RequestParam("file") file: MultipartFile) = try {
        val fileStream = file.inputStream
        studentService.importStudentsFromCsv(fileStream)
    } catch (e: Throwable) {
        e.printStackTrace()
        throw RepositoryException("Failed to save student")
    }
}