package cn.com.guardiantech.aofgo.backend.repo

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Session
import org.springframework.data.repository.CrudRepository
import java.util.*

interface SessionRepo : CrudRepository<Session, Long> {
    fun findById(id: Long): Optional<Session>
}