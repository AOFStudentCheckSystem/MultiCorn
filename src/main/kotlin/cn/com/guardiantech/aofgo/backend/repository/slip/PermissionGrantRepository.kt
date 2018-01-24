package cn.com.guardiantech.aofgo.backend.repository.slip

import cn.com.guardiantech.aofgo.backend.data.entity.slip.PermissionGrant
import org.springframework.data.repository.CrudRepository
import java.util.*

interface PermissionGrantRepository : CrudRepository<PermissionGrant, Long> {
    fun findById(id: Long): Optional<PermissionGrant>
}