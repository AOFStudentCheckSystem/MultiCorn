package cn.com.guardiantech.aofgo.backend.service

import cn.com.guardiantech.aofgo.backend.data.entity.GuardianType
import cn.com.guardiantech.aofgo.backend.data.entity.Student
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Subject
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
        val localLeaveRequestRepository: CampusLeaveRequestRepository,
        val permissionRequestRepository: PermissionRequestRepository
) {
    fun getPinkSlip(id: Long) = localLeaveRequestRepository.findById(id).get()

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
        return localLeaveRequestRepository.save(
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
                        ).toMutableSet<PermissionRequest>(),
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

    @Transactional
    fun studentSendPermissionRequests(id: Long): CampusLeaveRequest {
        val request = localLeaveRequestRepository.findById(id).get()
        request.permissionRequests.filterNot {
            it.acceptor.relation == GuardianType.ASSOCIATE_HEADMASTER
        }.forEach { sendPermissionRequestEmail(it) }
        request.evalWaitingStatus()
        return localLeaveRequestRepository.save(request)
    }

    fun sendPermissionRequestEmail(permissionRequest: PermissionRequest) {

    }

    fun permissionRequestStatusChange(subject: Subject) {

    }

    fun findLeaveRequestsByStatus(status: LeaveStatus) =
            localLeaveRequestRepository.findByStatus(status)

    @Transactional
    fun setLeaveRequestStatus(id: Long, leaveStatus: LeaveStatus): CampusLeaveRequest {
        val foundRequest = localLeaveRequestRepository.findById(id).get()
        foundRequest.setStatus(leaveStatus)
        return localLeaveRequestRepository.save(foundRequest)
    }
}