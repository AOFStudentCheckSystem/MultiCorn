package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.annotation.Require
import cn.com.guardiantech.aofgo.backend.authentication.AuthContext
import cn.com.guardiantech.aofgo.backend.data.entity.slip.CampusLeaveRequest
import cn.com.guardiantech.aofgo.backend.exception.EntityNotFoundException
import cn.com.guardiantech.aofgo.backend.jsonview.SlipView
import cn.com.guardiantech.aofgo.backend.request.slip.LeaveRequestStatusRequest
import cn.com.guardiantech.aofgo.backend.request.slip.LocalLeaveRequestRequest
import cn.com.guardiantech.aofgo.backend.request.slip.PermissionRequestRequest
import cn.com.guardiantech.aofgo.backend.service.PinkSlipService
import cn.com.guardiantech.aofgo.backend.service.StudentService
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
    @JsonView(SlipView.FullView::class)
    @Require(["LOCAL_LEAVE_REQUEST_STATUS_READ"])
    fun getPinkSlip(@PathVariable id: Long): CampusLeaveRequest {
        return pinkSlipService.getPinkSlip(id)
    }

    @PutMapping("/")
    @JsonView(SlipView.StudentView::class)
    @Require
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
    @JsonView(SlipView.StudentView::class)
    @Require
    fun initiateLocalLeaveRequest(@PathVariable id: Long, authContext: AuthContext) {
        val optRequest = pinkSlipService.getLeaveRequestOwnedBySubject(id, authContext.session!!.subject.id)
        if (!optRequest.isPresent) throw EntityNotFoundException("No owned campus leave request was found")
        pinkSlipService.studentSendPermissionRequests(optRequest.get())
    }

    @PutMapping("/permission")
    @JsonView(SlipView.FacultyView::class)
    @Require
    fun setPermissionRequestAccepted(@RequestBody @Valid req: PermissionRequestRequest, authContext: AuthContext, httpServletRequest: HttpServletRequest) {
        val optRequest = pinkSlipService.getPermissionRequestWithCorrectSubject(req.id, authContext.session!!.subject.id)
        if (!optRequest.isPresent) throw EntityNotFoundException("No owned permission request was found")
        return pinkSlipService.setPermissionRequestAccepted(optRequest.get(), req.accepted, httpServletRequest.remoteAddr)
    }

    @Require(["LOCAL_LEAVE_REQUEST_STATUS_WRITE"])
    @JsonView(SlipView.FacultyView::class)
    @PostMapping("/status/{id}")
    fun setLeaveRequestStatus(@PathVariable id: Long, @RequestBody @Valid statusRequest: LeaveRequestStatusRequest) {
        pinkSlipService.setLeaveRequestStatus(id, statusRequest.status)
    }

    @JsonView(SlipView.StudentView::class)
    @GetMapping("/own")
    @Require
    fun getOwnLeaveRequests(authContext: AuthContext, pageable: Pageable): Page<CampusLeaveRequest> {
        return pinkSlipService.getLocalLeaveRequestsBySubjectIdPaged(authContext.session!!.subject.id, pageable)
    }

    @JsonView(SlipView.FacultyView::class)
    @PostMapping("/status")
    @Require(["LOCAL_LEAVE_REQUEST_STATUS_READ"])
    fun getLeaveRequestsByStatus(@RequestBody @Valid statusRequest: LeaveRequestStatusRequest): Set<CampusLeaveRequest> {
        return pinkSlipService.getLeaveRequestByStatus(statusRequest.status)
    }
}