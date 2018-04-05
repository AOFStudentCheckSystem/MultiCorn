package cn.com.guardiantech.aofgo.backend.repository.slip

import cn.com.guardiantech.aofgo.backend.data.entity.slip.CampusLeaveRequest
import cn.com.guardiantech.aofgo.backend.data.entity.slip.LeaveStatus
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface CampusLeaveRequestRepository : CrudRepository<CampusLeaveRequest, Long> {
    fun findById(id: Long): Optional<CampusLeaveRequest>

    @Query("select r from #{#entityName} r WHERE r.id = ?1 AND r.student.account.subject.id = ?2")
    fun findByIdAndSubjectId(requestId: Long, subjectId: Long): Optional<CampusLeaveRequest>

    fun findByStatus(status: LeaveStatus): Set<CampusLeaveRequest>

    @Query("select r from #{#entityName} r WHERE r.student.account.subject.id = ?1")
    fun findBySubjectId(subjectId: Long): Set<CampusLeaveRequest>
}