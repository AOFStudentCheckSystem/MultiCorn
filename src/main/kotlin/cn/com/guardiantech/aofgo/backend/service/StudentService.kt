package cn.com.guardiantech.aofgo.backend.service

import cn.com.guardiantech.aofgo.backend.data.entity.Account
import cn.com.guardiantech.aofgo.backend.data.entity.AccountType
import cn.com.guardiantech.aofgo.backend.data.entity.Gender
import cn.com.guardiantech.aofgo.backend.data.entity.Student
import cn.com.guardiantech.aofgo.backend.exception.EntityNotFoundException
import cn.com.guardiantech.aofgo.backend.repository.StudentRepository
import cn.com.guardiantech.aofgo.backend.repository.auth.AccountRepository
import cn.com.guardiantech.aofgo.backend.request.student.StudentRequest
import com.opencsv.CSVParser
import com.opencsv.CSVReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.*

@Service
class StudentService @Autowired constructor(
        private val studentRepo: StudentRepository,
        private val accountService: AccountService,
        private val accountRepo: AccountRepository
) {
    /**
     * Failed to save student due to conflict
     */
    @Transactional
    fun createStudent(request: StudentRequest): Student {
        val theAccount: Account = if (request.accountId != null) {
            accountService.getAccountById(request.accountId)
        } else {
            if (request.account != null) {
                request.account.type = AccountType.STUDENT
                accountService.createAccount(request.account)
            } else {
                throw IllegalArgumentException("No account is provided with the student.")
            }
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

    /**
     * @throws NoSuchElementException Student Not Found
     * Failed to save student
     */
    @Transactional
    fun editStudent(request: StudentRequest): Student {
        val theStudent = studentRepo.findByIdNumber(request.idNumber).get()
        if (request.cardSecret != null) {
            theStudent.cardSecret = request.cardSecret
        }
        if (request.grade != null) {
            theStudent.grade = request.grade
        }
        if (request.gender != null) {
            theStudent.gender = request.gender
        }
        if (request.dateOfBirth != null) {
            theStudent.dateOfBirth = request.dateOfBirth
        }
        if (request.dorm != null) {
            theStudent.dorm = request.dorm
        }
        if (request.dormInfo != null) {
            theStudent.dormInfo = request.dormInfo
        }
        if (request.accountId != null && request.accountId != theStudent.account?.id) {
            theStudent.account = try {
                accountService.getAccountById(request.accountId)
            } catch (e: NoSuchElementException) {
                throw EntityNotFoundException("Account Not Found")
            }
        }
        return studentRepo.save(theStudent)
    }

    /**
     * @throws NoSuchElementException Student Not Found
     * Failed to save student
     */
    @Transactional
    fun editStudentCardSecret(idNumber: String, cardSecret: String?): Student =
            studentRepo.findByIdNumber(idNumber).get().let {
                it.cardSecret = cardSecret
                studentRepo.save(it)
            }

    @Transactional
    fun importStudentsFromCsv(csvContent: InputStream) {
        val csvReader: CSVReader = CSVReader((InputStreamReader(csvContent)), CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, 1)
        val records: List<Array<String>> = csvReader.readAll()

        for (record: Array<String> in records) {
            val newAccount: Account = accountRepo.save(
                    Account(
                            firstName = record[4],
                            lastName = record[7],
                            email = record[3],
                            phone = null,
                            type = AccountType.STUDENT,
                            preferredName = record[8],
                            subject = null
                    )
            )

            var processedCardSecret: String? = null
            if (record[1] == "NULL") {
            } else {
                processedCardSecret = record[1]
            }

            studentRepo.save(Student(
                    idNumber = record[6],
                    cardSecret = processedCardSecret,
                    grade = record[5].toInt(),
                    dateOfBirth = null,
                    gender = Gender.MALE,
                    dorm = record[2],
                    dormInfo = null,
                    account = newAccount
            ))
        }
    }
}