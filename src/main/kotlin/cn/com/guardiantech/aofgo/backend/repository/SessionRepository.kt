package cn.com.guardiantech.aofgo.backend.repository

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Session
import org.springframework.data.repository.CrudRepository
import java.util.*

interface SessionRepository : CrudRepository<Session, Long> {
    fun findById(id: Long): Optional<Session>
}