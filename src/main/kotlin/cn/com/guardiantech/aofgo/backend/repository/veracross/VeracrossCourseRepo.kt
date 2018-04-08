package cn.com.guardiantech.aofgo.backend.repository.veracross

import cn.com.guardiantech.aofgo.backend.data.entity.veracross.VeracrossCourse
import org.springframework.data.repository.CrudRepository
import java.util.*

interface VeracrossCourseRepo : CrudRepository<VeracrossCourse, Long> {
    fun findById(id: Long): Optional<VeracrossCourse>
}