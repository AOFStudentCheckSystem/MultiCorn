package cn.com.guardiantech.aofgo.backend.repository.slip

import cn.com.guardiantech.aofgo.backend.data.entity.slip.PermissionRequest
import org.springframework.data.repository.CrudRepository
import java.util.*

interface PermissionRequestRepository : CrudRepository<PermissionRequest, Long> {
    fun findById(id: Long): Optional<PermissionRequest>
}