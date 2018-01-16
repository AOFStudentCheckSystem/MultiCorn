package cn.com.guardiantech.aofgo.backend.repository.auth

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Subject
import org.springframework.data.repository.CrudRepository
import java.util.*

interface SubjectRepository : CrudRepository<Subject, Long> {
    fun findById(id: Long): Optional<Subject>
}