package cn.com.guardiantech.aofgo.backend.repository.veracross

import cn.com.guardiantech.aofgo.backend.data.entity.veracross.VeracrossCourseAttendee
import org.springframework.data.repository.CrudRepository
import java.util.*

interface VeracrossCourseAttendeeRepo : CrudRepository<VeracrossCourseAttendee, Long> {
    fun findById(id: Long): Optional<VeracrossCourseAttendee>
}