package cn.com.guardiantech.aofgo.backend.repository

import org.springframework.data.repository.CrudRepository
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Role
import java.util.*

interface RoleRepository : CrudRepository<Role, Long> {
    fun findById(id: Long): Optional<Role>
}

