package cn.com.guardiantech.aofgo.backend.repository

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Permission
import org.springframework.data.repository.CrudRepository
import java.util.*

interface PermissionRepository : CrudRepository<Permission, Long> {
    fun findById(id: Long): Optional<Permission>
}