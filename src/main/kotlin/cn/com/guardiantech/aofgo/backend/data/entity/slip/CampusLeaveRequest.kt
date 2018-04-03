package cn.com.guardiantech.aofgo.backend.data.entity.slip

import cn.com.guardiantech.aofgo.backend.data.entity.Student
import java.util.*
import javax.persistence.*

@Entity
class CampusLeaveRequest(
        @Id
        @GeneratedValue
        @Column(name = "id")
        val id: Long = -1,

        @OneToOne
        @JoinColumn(name = "student")
        val student: Student,

        @Enumerated(EnumType.STRING)
        @Column(name = "leave_type")
        val type: LeaveType,

        @Column(name = "description")
        val description: String,

        @Column(name = "status_message")
        val statusMessage: String,

        @OneToMany
        @JoinColumn(name = "permission_requests")
        val permissionRequests: MutableSet<PermissionRequest> = mutableSetOf(),

        @Enumerated(EnumType.STRING)
        @Column(name = "transportation_method")
        val transportationMethod: TransportationMethod,

        @Column(name = "transportation_note")
        val transportationNote: String,

        @Column(name = "date_of_leave")
        val dateOfLeave: Date,

        @Column(name = "date_of_return")
        val dateOfReturn: Date,

        @Column(name = "miss_class")
        val missClass: Boolean,

        @Column(name = "miss_sport")
        val missSport: Boolean,

        @Column(name = "miss_job")
        val missJob: Boolean,

        @Column(name = "contact_name")
        val contactName: String,

        @Column(name = "contact_phone")
        val contactPhone: String,

        @Column(name = "contact_address")
        val contactAddress: String,

        @Column(name = "status")
        @Enumerated(EnumType.STRING)
        private var status: LeaveStatus = LeaveStatus.PENDING,

        @Column(name = "submittedTime")
        val submittedTime: Date = Date()
) {

    fun addPermissionRequest(pr: PermissionRequest) {
        permissionRequests.add(pr)
        pr.campusLeaveRequest = this
    }

    fun setStatus(newStatus: LeaveStatus): LeaveStatus {
        if (status === LeaveStatus.PENDING) {
            if (newStatus !== LeaveStatus.PENDING) {
                throw IllegalArgumentException("Cannot revert state")
            }
        } else {
            if (newStatus === LeaveStatus.APPROVAL || newStatus === LeaveStatus.REJECTED) {
                status = newStatus
            } else {
                evalWaitingStatus()
            }
        }
        return status
    }

    fun evalWaitingStatus() {
        status = if (permissionRequests.all { it.accepted == true }) {
            LeaveStatus.WAITINGAPPROVAL
        } else {
            LeaveStatus.WAITINGPERMISSIONS
        }
    }
}