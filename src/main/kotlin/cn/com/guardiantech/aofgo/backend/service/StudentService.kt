package cn.com.guardiantech.aofgo.backend.service

import cn.com.guardiantech.aofgo.backend.data.entity.*
import cn.com.guardiantech.aofgo.backend.exception.EntityNotFoundException
import cn.com.guardiantech.aofgo.backend.repository.GuardianRepository
import cn.com.guardiantech.aofgo.backend.repository.StudentRepository
import cn.com.guardiantech.aofgo.backend.repository.auth.AccountRepository
import cn.com.guardiantech.aofgo.backend.request.student.GuardianRequest
import cn.com.guardiantech.aofgo.backend.request.student.StudentRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.*
import com.opencsv.enums.CSVReaderNullFieldIndicator
import com.opencsv.CSVReaderBuilder


@Service
class StudentService @Autowired constructor(
        private val studentRepo: StudentRepository,
        private val accountService: AccountService,
        private val guardianRepository: GuardianRepository,
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

        val s = studentRepo.save(Student(
                idNumber = request.idNumber,
                cardSecret = request.cardSecret,
                grade = request.grade,
                dateOfBirth = request.dateOfBirth,
                gender = request.gender,
                dorm = request.dorm,
                dormInfo = request.dormInfo,
                account = theAccount
        ))

        if (request.guardians !== null && request.guardians.isNotEmpty()) {
            guardianRepository.save(
                    request.guardians.map {
                        Guardian(
                                guardianAccount = accountService.getAccountById(it.accountId),
                                relation = it.relation
                        )
                    }
            ).forEach {
                s.addGuardian(it)
            }
            studentRepo.save(s)
        }

        return s
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

    fun findStudentBySubjectId(subjectId: Long): Student = studentRepo.findStudentBySubjectId(subjectId).get()

    @Transactional
    fun setGuardians(studentId: String, guardians: Set<GuardianRequest>): Set<Guardian> {
        val student = studentRepo.findByIdNumber(studentId).get()
        guardianRepository.save(guardians.map {
            Guardian(
                    guardianAccount = accountService.getAccountById(it.accountId),
                    relation = it.relation
            )
        }).forEach {
            student.addGuardian(it)
        }
        return studentRepo.save(student).guardians
    }

    @Transactional
    fun importStudentsFromCsv(csvContent: InputStream) {

        val csvReader = CSVReaderBuilder(InputStreamReader(csvContent))
                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                // Skip the header
                .withSkipLines(1)
                .build()

        val records: List<Array<String>> = csvReader.readAll()

        for (record: Array<String> in records) {

            var newAccount: Account

            try {
                newAccount = accountRepo.findByEmail(record[3]).get().let {
                    it.firstName = record[4]
                    it.lastName = record[7]
                    it.phone = null
                    it.type = AccountType.STUDENT
                    it.preferredName = record[8]
                    it.subject = null
                    accountRepo.save(it)
                }

            } catch (e: NoSuchElementException) {
                newAccount = accountRepo.save(
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
            }

            var processedCardSecret: String? = null
            if (record[1] == "NULL") {
            } else {
                processedCardSecret = record[1]

                try {
                    studentRepo.findByIdNumber(record[6]).get().let {
                        it.cardSecret = processedCardSecret
                        it.grade = record[5].toInt()
                        it.dateOfBirth = null
                        it.gender = Gender.MALE
                        it.dorm = record[2]
                        it.dormInfo = null
                        it.account = newAccount
                        studentRepo.save(it)
                    }
                    studentRepo.findByCardSecret(processedCardSecret).get().let {
                        it.idNumber = record[6]
                        it.cardSecret = processedCardSecret
                        it.grade = record[5].toInt()
                        it.dateOfBirth = null
                        it.gender = Gender.MALE
                        it.dorm = record[2]
                        it.dormInfo = null
                        it.account = newAccount
                        studentRepo.save(it)
                    }
                } catch (e: NoSuchElementException) {
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
    }
}