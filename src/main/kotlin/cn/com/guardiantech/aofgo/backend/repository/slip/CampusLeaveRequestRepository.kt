package cn.com.guardiantech.aofgo.backend.repository.slip

import cn.com.guardiantech.aofgo.backend.data.entity.slip.CampusLeaveRequest
import cn.com.guardiantech.aofgo.backend.data.entity.slip.LeaveStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.*

interface CampusLeaveRequestRepository : PagingAndSortingRepository<CampusLeaveRequest, Long> {
    fun findById(id: Long): Optional<CampusLeaveRequest>

    @Query("select r from #{#entityName} r WHERE r.id = ?1 AND r.student.account.subject.id = ?2")
    fun findByIdAndSubjectId(requestId: Long, subjectId: Long): Optional<CampusLeaveRequest>

    fun findByStatus(status: LeaveStatus): Set<CampusLeaveRequest>

    @Query("select r from #{#entityName} r WHERE r.student.account.subject.id = ?1")
    fun findBySubjectId(subjectId: Long): Set<CampusLeaveRequest>

    @Query("select r from #{#entityName} r WHERE r.student.account.subject.id = ?1")
    fun findBySubjectIdPaged(subjectId: Long, pageable: Pageable): Page<CampusLeaveRequest>
}