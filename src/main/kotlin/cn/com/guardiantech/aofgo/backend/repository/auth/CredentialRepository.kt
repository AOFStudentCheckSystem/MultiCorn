package cn.com.guardiantech.aofgo.backend.repository.auth

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Credential
import org.springframework.data.repository.CrudRepository
import java.util.*

interface CredentialRepository : CrudRepository<Credential, Long> {
    fun findById(id: Long): Optional<Credential>
}