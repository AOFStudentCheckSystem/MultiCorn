package cn.com.guardiantech.aofgo.backend.repository

import cn.com.guardiantech.aofgo.backend.data.entity.CampusLeaveRequest
import org.springframework.data.repository.CrudRepository
import java.util.*

interface CampusLeaveRequestRepository : CrudRepository<CampusLeaveRequest, Long> {
    fun findById(id: Long): Optional<CampusLeaveRequest>
}