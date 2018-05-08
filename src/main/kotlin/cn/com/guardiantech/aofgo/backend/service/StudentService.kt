package cn.com.guardiantech.aofgo.backend.service

import cn.com.guardiantech.aofgo.backend.data.entity.*
import cn.com.guardiantech.aofgo.backend.exception.BadRequestException
import cn.com.guardiantech.aofgo.backend.exception.EntityNotFoundException
import cn.com.guardiantech.aofgo.backend.repository.GuardianRepository
import cn.com.guardiantech.aofgo.backend.repository.StudentPagedRepository
import cn.com.guardiantech.aofgo.backend.repository.StudentRepository
import cn.com.guardiantech.aofgo.backend.repository.auth.AccountRepository
import cn.com.guardiantech.aofgo.backend.request.student.StudentRequest
import cn.com.guardiantech.aofgo.backend.request.student.StudentSearchColumn
import com.opencsv.CSVReaderBuilder
import com.opencsv.enums.CSVReaderNullFieldIndicator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.InputStream
import java.io.InputStreamReader


@Service
class StudentService @Autowired constructor(
        private val studentRepo: StudentRepository,
        private val studentPagedRepository: StudentPagedRepository,
        private val accountService: AccountService,
        private val guardianRepository: GuardianRepository,
        private val accountRepo: AccountRepository
) {

    @Autowired
    @Lazy
    private lateinit var guardianService: GuardianService

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
    fun importStudentsFromCsv(csvContent: InputStream) {
        val csvReader = CSVReaderBuilder(InputStreamReader(csvContent))
                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                // Skip the header
                .withSkipLines(1)
                .build()

        importStudentsFrom2DArray(csvReader.readAll())
    }

    @Transactional
    fun importStudentsFrom2DArray(records: List<Array<String>>) {
        for (record: Array<String> in records) {
            val newAccount =
                    accountRepo.findByEmail(record[3]).let {
                        if (it.isPresent) {
                            it.get().let {
                                it.firstName = record[4]
                                it.lastName = record[7]
                                it.phone = null
                                it.type = AccountType.STUDENT
                                it.preferredName = record[8]
                                it.subject = null
                                accountRepo.save(it)
                            }
                        } else {
                            accountRepo.save(
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
                    }

            val processedCardSecret = if (record[1] == "NULL") null else record[1]
            if (processedCardSecret !== null) {
                studentRepo.findByCardSecret(processedCardSecret).let {
                    if (it.isPresent) {
                        it.get().cardSecret = null
                        studentRepo.save(it.get())
                    }
                }
            }

            studentRepo.findByIdNumber(record[6]).let {
                if (it.isPresent) {
                    it.get().let {
                        it.cardSecret = processedCardSecret
                        it.grade = record[5].toInt()
                        it.dateOfBirth = null
                        it.gender = Gender.MALE
                        it.dorm = record[2]
                        it.dormInfo = null
                        it.account = newAccount
                        studentRepo.save(it)
                    }
                } else {
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

    @Transactional
    fun importGuardians(importGuardians: List<Array<String>>) {
        val processedSet = HashSet<Triple<Student, Account, GuardianType>>()
        importGuardians.forEachIndexed { index, row ->
            if (row.size != 3) throw BadRequestException("Row ${index + 2} in csv file doesn't have three items")
            val studentEmail = findStudentByAccountEmail(row[0]).let {
                if (it.isPresent) it.get() else throw BadRequestException("Row ${index + 2} in csv file have unregistered student email")
            }
            val guardianEmail = accountRepo.findByEmail(row[1]).let {
                if (it.isPresent) it.get() else throw BadRequestException("Row ${index + 2} in csv file have unregistered guardian email")
            }
            val relation = try {
                GuardianType.valueOf(row[2])
            } catch (e: Throwable) {
                throw BadRequestException("Row ${index + 2} in csv file contains invalid relation")
            }
            processedSet.add(Triple(studentEmail, guardianEmail, relation))
        }

        processedSet.forEach {
            guardianService.addExistingOrNewGuardianToStudent(
                    it.first.idNumber,
                    it.second.id,
                    it.third
            )
        }
    }

    fun findStudentByAccountEmail(email: String) = studentRepo.findStudentByAccountEmail(email)

    fun findStudentFiltered(column: StudentSearchColumn, search: String, pageable: Pageable): Page<Student> {
        return when (column) {
            StudentSearchColumn.FUZZY -> studentPagedRepository.fuzzySearch(search, pageable)
            StudentSearchColumn.FIRST_NAME -> studentPagedRepository.searchByFirstName(search, pageable)
            StudentSearchColumn.ID_NUMBER -> studentPagedRepository.searchByIdNumber(search, pageable)
            StudentSearchColumn.PREFERRED_NAME -> studentPagedRepository.searchByPreferredName(search, pageable)
            StudentSearchColumn.LAST_NAME -> studentPagedRepository.searchByLastName(search, pageable)
        }
    }
}