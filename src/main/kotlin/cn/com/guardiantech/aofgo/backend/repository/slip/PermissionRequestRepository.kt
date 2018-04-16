package cn.com.guardiantech.aofgo.backend.repository.slip

import cn.com.guardiantech.aofgo.backend.data.entity.slip.PermissionRequest
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface PermissionRequestRepository : CrudRepository<PermissionRequest, Long> {
    fun findById(id: Long): Optional<PermissionRequest>

    @Query("select r from #{#entityName} r WHERE r.id = ?1 AND r.acceptor.guardianAccount.subject.id = ?2")
    fun findByIdAndSubjectId(requestId: Long, subjectId: Long): Optional<PermissionRequest>

}