package cn.com.guardiantech.aofgo.backend.repository.checkin

import cn.com.guardiantech.aofgo.backend.data.entity.checkin.ActivityEvent
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.EventStatus
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.*
import javax.transaction.Transactional

/**
 * Created by Codetector on 2017/4/4.
 * Project backend
 */
interface EventRepository : PagingAndSortingRepository<ActivityEvent, Long>{
    override fun findAll(): MutableList<ActivityEvent>

    @Modifying
    @Transactional
    fun removeByEventId(eventID: String): Long

    fun findByEventId(eventID: String): Optional<ActivityEvent>

    fun findByEventStatus(status: EventStatus): Set<ActivityEvent>
}