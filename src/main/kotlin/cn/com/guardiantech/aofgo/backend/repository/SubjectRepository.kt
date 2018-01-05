package cn.com.guardiantech.aofgo.backend.repository

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Subject
import org.springframework.data.repository.CrudRepository
import java.util.*

interface SubjectRepository : CrudRepository<Subject, Long> {
    fun findById(id: Long): Optional<Subject>
}