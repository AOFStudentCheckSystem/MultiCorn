package cn.com.guardiantech.aofgo.backend.data.entity

import org.hibernate.secure.spi.GrantedPermission
import java.util.*
import javax.persistence.*

class CampusLeaveRequest(
        @Id
        @GeneratedValue
        @Column(name = "id")
        val id: Long = -1,

        @OneToOne
        @JoinColumn(name = "student")
        val student: Student,

        @Enumerated(EnumType.STRING)
        @Column(name = "type")
        val type: LeaveType,

        @Column(name = "description")
        val description: String,

        @Enumerated(EnumType.STRING)
        @Column(name = "status")
        val status: LeaveStatus,

        @Column(name = "status_message")
        val statusMessage: String,

        @Column(name = "required_permission")
        val requiredPermission: MutableSet<String>,

        @OneToMany
        @JoinColumn(name = "granted_permission")
        val grantedPermission: MutableSet<PermissionGrant> = hashSetOf(),

        @Enumerated(EnumType.STRING)
        @Column(name = "transportation_method")
        val transportationMethod: TransportationMethod,

        @Column(name = "transportation-note")
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
        val contactAddress: String

)