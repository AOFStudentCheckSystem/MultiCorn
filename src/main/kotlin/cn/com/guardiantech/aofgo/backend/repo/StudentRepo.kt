package cn.com.guardiantech.aofgo.backend.repo

import cn.com.guardiantech.aofgo.backend.data.entity.Student
import org.springframework.data.repository.CrudRepository
import java.util.*

interface StudentRepo : CrudRepository<Student, Long> {
    fun findById(id: Long): Optional<Student>
}