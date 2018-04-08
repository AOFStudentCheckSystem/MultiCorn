package cn.com.guardiantech.aofgo.backend.repository.veracross

import cn.com.guardiantech.aofgo.backend.data.entity.veracross.VeracrossCookie
import org.springframework.data.repository.CrudRepository
import java.util.*

interface VeracrossCookieRepo : CrudRepository<VeracrossCookie, Long> {
    fun findById(id: Long): Optional<VeracrossCookie>
}