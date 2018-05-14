package cn.com.guardiantech.aofgo.backend.repository.slip

import cn.com.guardiantech.aofgo.backend.data.entity.slip.PermissionRequestToken
import org.springframework.data.repository.CrudRepository
import java.util.*

interface PermissionRequestTokenRepository : CrudRepository<PermissionRequestToken, Long> {
    fun findById(id: Long): Optional<PermissionRequestToken>

    fun findByToken(token: String): Optional<PermissionRequestToken>
}