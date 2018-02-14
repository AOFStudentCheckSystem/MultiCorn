package cn.com.guardiantech.aofgo.backend.repository.auth

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Permission
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.PermissionType
import org.springframework.data.repository.CrudRepository
import java.util.*

interface PermissionRepository : CrudRepository<Permission, Long> {
    fun findById(id: Long): Optional<Permission>
    fun findByPermissionKey(permission: String): Optional<Permission>
    fun findAllByOrderByPermissionKeyAsc(): List<Permission>
    fun findByPermissionType(type: PermissionType): List<Permission>
}