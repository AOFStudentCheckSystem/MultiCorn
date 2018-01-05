package cn.com.guardiantech.aofgo.backend.repo

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Principal
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.PrincipalType
import org.springframework.data.repository.CrudRepository
import java.util.*

interface PrincipalRepo : CrudRepository<Principal, Long> {
    fun findById(id: Long): Optional<Principal>

    fun findByTypeAndIdentification(type: PrincipalType, identification: String): Optional<Principal>
}