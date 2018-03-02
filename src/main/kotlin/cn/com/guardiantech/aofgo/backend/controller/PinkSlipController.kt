package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.authentication.AuthContext
import cn.com.guardiantech.aofgo.backend.data.entity.slip.CampusLeaveRequest
import cn.com.guardiantech.aofgo.backend.request.slip.LocalLeaveRequestRequest
import cn.com.guardiantech.aofgo.backend.service.PinkSlipService
import cn.com.guardiantech.aofgo.backend.service.StudentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * Created by dedztbh on 18-1-24.
 * Project AOFGoBackend
 */

@RestController
@RequestMapping("/slip")
class PinkSlipController @Autowired constructor(
        val pinkSlipService: PinkSlipService,
        val studentService: StudentService
) {
    @GetMapping("/{id}")
    fun getPinkSlip(@PathVariable id: Long): CampusLeaveRequest {
        return pinkSlipService.getPinkSlip(id)
    }

    @PutMapping("/")
    fun addLocalLeaveRequest(@RequestBody r: LocalLeaveRequestRequest, authContext: AuthContext): CampusLeaveRequest {
        return pinkSlipService.addLocalLeaveRequest(
                student = studentService.findStudentBySubjectId(authContext.session!!.subject.id),
                type = r.type,
                description = r.description,
                statusMessage = r.statusMessage,
                transportationMethod = r.transportationMethod,
                transportationNote = r.transportationNote,
                dateOfLeave = r.dateOfLeave,
                dateOfReturn = r.dateOfReturn,
                missClass = r.missClass,
                missSport = r.missSport,
                missJob = r.missJob,
                contactName = r.contactName,
                contactPhone = r.contactPhone,
                contactAddress = r.contactAddress
        )
    }

    @PutMapping("/init/{id}")
    fun initiateLocalLeaveRequest(@PathVariable id: Long, authContext: AuthContext) {
        pinkSlipService.studentSendPermissionRequests(id)
    }
}