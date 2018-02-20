package cn.com.guardiantech.aofgo.backend.service

import cn.com.guardiantech.aofgo.backend.data.entity.slip.LeaveType
import cn.com.guardiantech.aofgo.backend.data.entity.slip.TransportationMethod
import cn.com.guardiantech.aofgo.backend.repository.slip.CampusLeaveRequestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

/**
 * Created by dedztbh on 18-1-24.
 * Project AOFGoBackend
 */

@Service
class PinkSlipService @Autowired constructor(
        val localLeaveRequestRepo: CampusLeaveRequestRepository
) {
    fun getPinkSlip(id: Long) = localLeaveRequestRepo.findById(id).get()

    fun addLocalLeaveRequest(
            student: Long,
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
    ) {

    }
}