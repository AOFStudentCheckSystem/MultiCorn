package cn.com.guardiantech.aofgo.backend.repository

import cn.com.guardiantech.aofgo.backend.data.entity.Guardian
import org.springframework.data.repository.CrudRepository
import java.util.*

interface GuardianRepository : CrudRepository<Guardian, Long> {
    fun findById(id: Long): Optional<Guardian>
}