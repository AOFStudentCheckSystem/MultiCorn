package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.annotation.Require
import cn.com.guardiantech.aofgo.backend.data.entity.Student
import cn.com.guardiantech.aofgo.backend.exception.*
import cn.com.guardiantech.aofgo.backend.repository.StudentPagedRepository
import cn.com.guardiantech.aofgo.backend.request.student.StudentEditCardSecretRequest
import cn.com.guardiantech.aofgo.backend.request.student.StudentRequest
import cn.com.guardiantech.aofgo.backend.request.student.StudentSearchRequest
import cn.com.guardiantech.aofgo.backend.service.StudentService
import com.opencsv.CSVReaderBuilder
import com.opencsv.enums.CSVReaderNullFieldIndicator
import javassist.NotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.InputStreamReader
import java.sql.SQLException
import javax.servlet.ServletContext
import javax.validation.Valid


@RestController
@RequestMapping("/student")
class StudentController @Autowired constructor(
        @Autowired
        val context: ServletContext,
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
        logger.error(e.message)
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

    @Require(["STUDENT_WRITE", "ACCOUNT_WRITE"])
    @PostMapping("/import")
    @ResponseBody
    fun importStudentsFromCsv(
            @RequestParam("file") file: MultipartFile) = try {
        val fileStream = file.inputStream
        studentService.importStudentsFromCsv(fileStream)
    } catch (e: SQLException) {
        throw ImportBadConstraintException("Constraint violation; check for duplicate entries")
    } catch (e: Throwable) {
        throw RepositoryException("Failed to save students")
    }

    @Require(["STUDENT_WRITE", "GUARDIAN_WRITE"])
    @PostMapping("/guardian/import")
    @ResponseBody
    fun importGuardiansFromCsv(
            @RequestParam("file") file: MultipartFile) = try {
        studentService.importGuardians(
                CSVReaderBuilder(InputStreamReader(file.inputStream))
                        .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                        // Skip the header
                        .withSkipLines(1)
                        .build()
                        .readAll()
        )
    } catch (e: SQLException) {
        throw ImportBadConstraintException("Constraint violation; check for duplicate entries")
    } catch (e: Throwable) {
        when (e) {
            is ControllerException -> {
                throw e
            }
            else -> {
                throw RepositoryException("Failed to save students")
            }
        }
    }

    @Require(["STUDENT_READ"])
    @PostMapping("/search")
    fun findStudentFiltered(@RequestBody @Valid request: StudentSearchRequest, pageable: Pageable): Page<Student> {
        return studentService.findStudentFiltered(request.column, request.search, pageable)
    }
}