package cn.com.guardiantech.aofgo.backend.repository

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Principal
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.PrincipalType
import org.springframework.data.repository.CrudRepository
import java.util.*

interface PrincipalRepository : CrudRepository<Principal, Long> {
    fun findById(id: Long): Optional<Principal>

    fun findByTypeAndIdentification(type: PrincipalType, identification: String): Optional<Principal>
}