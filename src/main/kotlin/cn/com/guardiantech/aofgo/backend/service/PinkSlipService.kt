package cn.com.guardiantech.aofgo.backend.service

import cn.com.guardiantech.aofgo.backend.data.entity.GuardianType
import cn.com.guardiantech.aofgo.backend.data.entity.Student
import cn.com.guardiantech.aofgo.backend.data.entity.slip.*
import cn.com.guardiantech.aofgo.backend.repository.slip.CampusLeaveRequestRepository
import cn.com.guardiantech.aofgo.backend.repository.slip.PermissionRequestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Created by dedztbh on 18-1-24.
 * Project AOFGoBackend
 */

@Service
class PinkSlipService @Autowired constructor(
        val leaveRequestRepository: CampusLeaveRequestRepository,
        val permissionRequestRepository: PermissionRequestRepository
) {
    fun getPinkSlip(id: Long) = leaveRequestRepository.findById(id).get()

    @Transactional
    fun addLocalLeaveRequest(
            student: Student,
            type: LeaveType,
            description: String,
            statusMessage: String,
            transportationMethod: TransportationMethod,
            transportationNote: String,
            dateOfLeave: Date,
            dateOfReturn: Date,
            missClass: Boolean,
            missSport: Boolean,
            missJob: Boolean,
            contactName: String,
            contactPhone: String,
            contactAddress: String
    ): CampusLeaveRequest {
        return leaveRequestRepository.save(
                CampusLeaveRequest(
                        student = student,
                        type = type,
                        description = description,
                        statusMessage = statusMessage,
                        transportationMethod = transportationMethod,
                        transportationNote = transportationNote,
                        permissionRequests = permissionRequestRepository.save(
                                student.guardians.filter {
                                    when (it.relation) {
                                        GuardianType.ACADEMIC_DEAN -> missClass
                                        GuardianType.COACH -> missSport
                                        GuardianType.FACULTY_SUPERVISOR -> missJob
                                        else -> true
                                    }
                                }.map {
                                    PermissionRequest(
                                            acceptor = it
                                    )
                                }
                        ).toMutableSet(),
                        dateOfLeave = dateOfLeave,
                        dateOfReturn = dateOfReturn,
                        missClass = missClass,
                        missSport = missSport,
                        missJob = missJob,
                        contactName = contactName,
                        contactPhone = contactPhone,
                        contactAddress = contactAddress,
                        status = LeaveStatus.PENDING
                )
        )
    }

    fun getLeaveRequestOwnedBySubject(leaveRequestId: Long, subjectId: Long) =
            leaveRequestRepository.findByIdAndSubjectId(leaveRequestId, subjectId)


    @Transactional
    fun studentSendPermissionRequests(request: CampusLeaveRequest): CampusLeaveRequest {
        request.permissionRequests.filterNot {
            it.acceptor.relation == GuardianType.ASSOCIATE_HEADMASTER
        }.forEach { sendPermissionRequestEmail(it) }
        request.evalWaitingStatus()
        return leaveRequestRepository.save(request)
    }

    fun sendPermissionRequestEmail(permissionRequest: PermissionRequest) {

    }

    fun getPermissionRequestRequiredBySubject(permissionRequestId: Long, subjectId: Long) =
            permissionRequestRepository.findByIdAndSubjectId(permissionRequestId, subjectId)

    @Transactional
    fun setPermissionRequestAccepted(permissionRequest: PermissionRequest, accepted: Boolean, ip: String) {
        permissionRequest.accepted = accepted
        permissionRequest.acceptTime = Date()
        permissionRequest.acceptorIp = ip
        permissionRequestRepository.save(permissionRequest)
    }

    fun findLeaveRequestsByStatus(status: LeaveStatus) =
            leaveRequestRepository.findByStatus(status)

    @Transactional
    fun setLeaveRequestStatus(id: Long, leaveStatus: LeaveStatus): CampusLeaveRequest {
        val foundRequest = leaveRequestRepository.findById(id).get()
        foundRequest.setStatus(leaveStatus)
        return leaveRequestRepository.save(foundRequest)
    }
}