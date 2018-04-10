package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.annotation.Require
import cn.com.guardiantech.aofgo.backend.data.entity.Guardian
import cn.com.guardiantech.aofgo.backend.request.student.GuardianRequest
import cn.com.guardiantech.aofgo.backend.service.GuardianService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
@RequestMapping("/student/{studentId}/guardian")
class GuardianController @Autowired constructor(
        val guardianService: GuardianService
) {
    private val logger: Logger = LoggerFactory.getLogger(GuardianController::class.java)

    @Require(["STUDENT_WRITE"])
    @PostMapping("/set")
    fun setGuardians(@PathVariable studentId: String, @RequestBody @Valid guardians: Array<GuardianRequest>): Set<Guardian> {
//        if (r.guardians == null) throw BadRequestException("Guardians Not Found")
        return guardianService.setGuardians(
                studentId,
                guardians.toSet()
        )
    }

    @Require(["STUDENT_WRITE"])
    @PutMapping("/")
    fun addExistingOrNewGuardianToStudent(@PathVariable studentId: String, @RequestBody @Valid guardian: GuardianRequest) {
        return guardianService.addExistingOrNewGuardianToStudent(
                studentId,
                guardian.accountId,
                guardian.relation
        )
    }
}