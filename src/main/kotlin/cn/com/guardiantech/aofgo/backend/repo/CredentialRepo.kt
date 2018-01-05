package cn.com.guardiantech.aofgo.backend.repo

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Credential
import org.springframework.data.repository.CrudRepository
import java.util.*

interface CredentialRepo : CrudRepository<Credential, Long> {
    fun findById(id: Long): Optional<Credential>
}