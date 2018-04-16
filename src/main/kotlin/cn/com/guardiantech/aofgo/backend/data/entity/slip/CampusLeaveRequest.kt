package cn.com.guardiantech.aofgo.backend.data.entity.slip

import cn.com.guardiantech.aofgo.backend.data.entity.Student
import cn.com.guardiantech.aofgo.backend.jsonview.SlipView
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonView
import java.util.*
import javax.persistence.*

@Entity
class CampusLeaveRequest(
        @Id
        @GeneratedValue
        @Column(name = "id")
        @JsonView(SlipView.StudentView::class)
        val id: Long = -1,

        @OneToOne
        @JoinColumn(name = "student")
        @JsonView(SlipView.FullView::class)
        val student: Student,

        @Enumerated(EnumType.STRING)
        @Column(name = "leave_type")
        @JsonView(SlipView.StudentView::class)
        val type: LeaveType,

        @Column(name = "visit_person")
        @JsonView(SlipView.StudentView::class)
        val visitPerson: String?,

        @Column(name = "description")
        @JsonView(SlipView.StudentView::class)
        val description: String,

        @Column(name = "status_message")
        @JsonView(SlipView.StudentView::class)
        val statusMessage: String,

        @OneToMany
        @JoinColumn(name = "permission_requests")
        @JsonView(SlipView.FullView::class)
        val permissionRequests: MutableSet<PermissionRequest> = mutableSetOf(),

        @Enumerated(EnumType.STRING)
        @Column(name = "transportation_method")
        @JsonView(SlipView.StudentView::class)
        val transportationMethod: TransportationMethod,

        @Column(name = "transportation_note")
        @JsonView(SlipView.StudentView::class)
        val transportationNote: String,

        @Column(name = "date_of_leave")
        @JsonView(SlipView.StudentView::class)
        val dateOfLeave: Date,

        @Column(name = "date_of_return")
        @JsonView(SlipView.StudentView::class)
        val dateOfReturn: Date,

        @Column(name = "miss_class")
        @JsonView(SlipView.StudentView::class)
        val missClass: Boolean,

        @Column(name = "miss_sport")
        @JsonView(SlipView.StudentView::class)
        val missSport: Boolean,

        @Column(name = "miss_job")
        @JsonView(SlipView.StudentView::class)
        val missJob: Boolean,

        @Column(name = "contact_name")
        @JsonView(SlipView.StudentView::class)
        val contactName: String,

        @Column(name = "contact_phone")
        @JsonView(SlipView.StudentView::class)
        val contactPhone: String,

        @Column(name = "contact_address")
        @JsonView(SlipView.StudentView::class)
        val contactAddress: String,

        @Column(name = "status")
        @Enumerated(EnumType.STRING)
        @JsonView(SlipView.StudentView::class)
        var status: LeaveStatus = LeaveStatus.PENDING,

        @Column(name = "submittedTime")
        @JsonView(SlipView.StudentView::class)
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

    private fun evalWaitingStatus() {
        status = if (permissionRequests.all { it.accepted == true }) {
            LeaveStatus.WAITINGAPPROVAL
        } else {
            LeaveStatus.WAITINGPERMISSIONS
        }
    }

    @JsonProperty("studentName")
    @JsonView(SlipView.FacultyView::class)
    fun getStudentName(): String {
        return student.account?.let {
            it.firstName + " " + it.lastName
        } ?: "Unknown name"
    }
}