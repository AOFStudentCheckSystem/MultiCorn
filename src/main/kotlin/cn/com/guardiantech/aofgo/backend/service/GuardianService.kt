package cn.com.guardiantech.aofgo.backend.service

import cn.com.guardiantech.aofgo.backend.data.entity.Guardian
import cn.com.guardiantech.aofgo.backend.data.entity.GuardianType
import cn.com.guardiantech.aofgo.backend.data.entity.Student
import cn.com.guardiantech.aofgo.backend.repository.GuardianRepository
import cn.com.guardiantech.aofgo.backend.repository.StudentRepository
import cn.com.guardiantech.aofgo.backend.request.student.GuardianRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class GuardianService @Autowired constructor(
        private val studentRepo: StudentRepository,
        private val studentSerive: StudentService,
        private val accountService: AccountService,
        private val guardianRepository: GuardianRepository
) {
    @Transactional
    fun setGuardians(studentId: String, guardians: Set<GuardianRequest>): Set<Guardian> {
        val student = studentRepo.findByIdNumber(studentId).get()
        val oldGuardianAccountIds = student.guardians.map { it.guardianAccount.id }
        val newGuardianAccountIds = guardians.map { it.accountId }

        val guardianAccountIdsToRemove =
                oldGuardianAccountIds.filter { !newGuardianAccountIds.contains(it) }
        val guardiansToAdd =
                guardians.filter { !oldGuardianAccountIds.contains(it.accountId) }
        val guardiansToUpdate =
                guardiansToAdd.map { it.accountId }.let { guardianIdsToAdd ->
                    guardians.filterNot {
                        guardianAccountIdsToRemove.contains(it.accountId) ||
                                guardianIdsToAdd.contains(it.accountId)
                    }
                }

        guardianAccountIdsToRemove.let {
            if (it.isNotEmpty()) {
                it.forEach {
                    student.removeGuardian(it)
                }
//                student = studentRepo.save(student)
            }
        }
        guardiansToUpdate.let {
            if (it.isNotEmpty()) {
                it.forEach {
                    guardianRepository.save(
                            student.updateGuardian(it.accountId, it.relation)
                    )
                }
            }
        }
        guardianRepository.save(guardiansToAdd.map {
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
    fun addExistingOrNewGuardianToStudent(studentId: String, guardianAccountId: Long, guardianRelation: GuardianType) {
        val student = studentRepo.findByIdNumber(studentId).get()
        val guardianOpt = guardianRepository.findByAccountId(guardianAccountId)
        if (guardianOpt.isPresent) {
            val guardian = guardianOpt.get()
            if (student.guardians.none { it.id == guardian.id }) {
                student.guardians.add(guardian)
                studentRepo.save(student)
            }
        } else {
            val guardian = guardianRepository.save(
                    Guardian(
                            guardianAccount = accountService.getAccountById(guardianAccountId),
                            relation = guardianRelation
                    )
            )
            student.guardians.add(guardian)
            studentRepo.save(student)
        }
    }

    @Transactional
    fun deleteGuardians(studentId: String, guardianId: Long): Student {
        return studentSerive.getStudentByIdNumber(studentId).let {
            it.guardians.removeIf {
                it.id == guardianId
            }
            studentRepo.save(it)
        }
    }
}