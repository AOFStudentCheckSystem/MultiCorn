package cn.com.guardiantech.aofgo.backend.request.slip

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import cn.com.guardiantech.aofgo.backend.data.entity.slip.LeaveType
import cn.com.guardiantech.aofgo.backend.data.entity.slip.TransportationMethod
import java.util.*
import javax.validation.constraints.NotNull

/**
 * Created by dedztbh on 18-2-19.
 * Project AOFGoBackend
 */
@NoArg
class LocalLeaveRequestRequest(
        val id: Long?,

        val student: Long?,

        @field:NotNull
        val type: LeaveType,

        val description: String = "",

        val statusMessage: String = "",

//        val permissionRequests: MutableSet<PermissionRequest>,

        @field:NotNull
        val transportationMethod: TransportationMethod,

        val transportationNote: String = "",

        @field:NotNull
        val dateOfLeave: Date,

        @field:NotNull
        val dateOfReturn: Date,

        @field:NotNull
        val missClass: Boolean,

        @field:NotNull
        val missSport: Boolean,

        @field:NotNull
        val missJob: Boolean,

        val contactName: String = "",

        val contactPhone: String = "",

        val contactAddress: String = ""
)