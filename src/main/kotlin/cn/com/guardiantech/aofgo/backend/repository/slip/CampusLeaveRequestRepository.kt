package cn.com.guardiantech.aofgo.backend.repository.slip

import cn.com.guardiantech.aofgo.backend.data.entity.slip.CampusLeaveRequest
import cn.com.guardiantech.aofgo.backend.data.entity.slip.LeaveStatus
import org.springframework.data.repository.CrudRepository
import java.util.*

interface CampusLeaveRequestRepository : CrudRepository<CampusLeaveRequest, Long> {
    fun findById(id: Long): Optional<CampusLeaveRequest>

    fun findByStatus(status: LeaveStatus): Set<CampusLeaveRequest>
}