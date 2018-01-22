package cn.com.guardiantech.aofgo.backend.repository.auth

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Role
import org.springframework.data.repository.CrudRepository
import java.util.*

interface RoleRepository : CrudRepository<Role, Long> {
    fun findById(id: Long): Optional<Role>
    fun findByRoleName(name: String): Optional<Role>
    fun findAllByOrderByRoleNameAsc(): List<Role>
}

