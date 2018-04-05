package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.annotation.Require
import cn.com.guardiantech.aofgo.backend.authentication.AuthContext
import cn.com.guardiantech.aofgo.backend.data.entity.slip.CampusLeaveRequest
import cn.com.guardiantech.aofgo.backend.exception.EntityNotFoundException
import cn.com.guardiantech.aofgo.backend.request.slip.LeaveRequestStatusRequest
import cn.com.guardiantech.aofgo.backend.request.slip.LocalLeaveRequestRequest
import cn.com.guardiantech.aofgo.backend.request.slip.PermissionRequestRequest
import cn.com.guardiantech.aofgo.backend.service.PinkSlipService
import cn.com.guardiantech.aofgo.backend.service.StudentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

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
    fun initiateLocalLeaveRequest(@PathVariable id: Long, authContext: AuthContext): CampusLeaveRequest {
        val optRequest = pinkSlipService.getLeaveRequestOwnedBySubject(id, authContext.session!!.subject.id)
        if (!optRequest.isPresent) throw EntityNotFoundException("No owned campus leave request was found")
        return pinkSlipService.studentSendPermissionRequests(optRequest.get())
    }

    @PutMapping("/permission")
    fun setPermissionRequestAccepted(@RequestBody @Valid req: PermissionRequestRequest, authContext: AuthContext, httpServletRequest: HttpServletRequest) {
        val optRequest = pinkSlipService.getPermissionRequestRequiredBySubject(req.id, authContext.session!!.subject.id)
        if (!optRequest.isPresent) throw EntityNotFoundException("No owned permission request was found")
        return pinkSlipService.setPermissionRequestAccepted(optRequest.get(), req.accepted, httpServletRequest.remoteAddr)
    }

    @Require(["LOCAL_LEAVE_REQUEST_STATUS_WRITE"])
    @PostMapping("/status/{id}")
    fun setLeaveRequestStatus(@PathVariable id: Long, @RequestBody @Valid statusRequest: LeaveRequestStatusRequest) {
        pinkSlipService.setLeaveRequestStatus(id, statusRequest.status)
    }

    @GetMapping("/own")
    fun getOwnLeaveRequests(authContext: AuthContext): Set<CampusLeaveRequest> {
        return pinkSlipService.getLocalLeaveRequestsBySubjectId(authContext.session!!.subject.id)
    }
}