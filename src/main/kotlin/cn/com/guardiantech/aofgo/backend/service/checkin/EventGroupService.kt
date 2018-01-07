package cn.com.guardiantech.aofgo.backend.service.checkin

import cn.com.guardiantech.aofgo.backend.data.entity.checkin.ActivityEvent
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.EventGroup
import cn.com.guardiantech.aofgo.backend.repository.checkin.ActivityEventRepository
import cn.com.guardiantech.aofgo.backend.repository.checkin.EventGroupRepository
import cn.com.guardiantech.aofgo.backend.request.checkin.CREventToGroupRequest
import cn.com.guardiantech.aofgo.backend.request.checkin.GroupRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import java.util.*

/**
 * Created by dedztbh on 1/6/18.
 * Project AOFGoBackend
 */

@Service
class EventGroupService @Autowired constructor(
        private val eventRepo: ActivityEventRepository,
        private val eventGroupRepo: EventGroupRepository
) {

    /**
     * @throws NoSuchElementException Group not found
     */
    fun createGroup(request: GroupRequest) {
        val events: MutableSet<ActivityEvent> = hashSetOf()
        request.groupItems.forEach {
            events.add(eventRepo.findByEventId(it).get())
        }
        val group = eventGroupRepo.save(EventGroup(name = request.name, events = events))
    }

    /**
     * @throws NoSuchElementException Group or event not found
     */
    fun addEventToGroup(request: CREventToGroupRequest, groupId: Long) {
        val targetGroup = eventGroupRepo.findById(groupId).get()
        val targetEvent = eventRepo.findByEventId(request.eventId).get()
        targetGroup.events.add(targetEvent)
        eventGroupRepo.save(targetGroup)
    }

    /**
     * @throws NoSuchElementException Group or event not found
     */
    fun removeEventFromGroup(request: CREventToGroupRequest, groupId: Long) {
        val targetGroup = eventGroupRepo.findById(groupId).get()
        val targetEvent = eventRepo.findByEventId(request.eventId).get()
        val result = targetGroup.events.remove(targetEvent)
        eventGroupRepo.save(targetGroup)
    }

    /**
     * @throws NoSuchElementException Group or event not found
     */
    fun setEventToGroup(items: Array<Long>, groupId: Long) {
        val targetGroup = eventGroupRepo.findById(groupId).get()
        val events: MutableList<ActivityEvent> = arrayListOf()
        items.forEach {
            events.add(eventRepo.findByEventId(it.toString()).get())
        }
        targetGroup.events.clear()
        targetGroup.events.addAll(events)
        eventGroupRepo.save(targetGroup)
    }

    /**
     * @throws NoSuchElementException group not found
     */
    fun updateEventTitle(request: GroupRequest, groupId: Long) {
        val targetGroup = eventGroupRepo.findById(groupId).get()
        targetGroup.name = request.name
        eventGroupRepo.save(targetGroup)
    }

    fun removeEventGroupById(id: Long): Long =
            eventGroupRepo.removeById(id)

    fun listEventGroups(pageable: Pageable): Page<EventGroup> =
            eventGroupRepo.findAllByOrderByIdDesc(pageable)

    /**
     * @throws NoSuchElementException group not found
     */
    fun getEventGroupById(@PathVariable id: Long): EventGroup =
            eventGroupRepo.findById(id).get()

    fun listAvailable(): List<EventGroup> =
            eventGroupRepo.findGroupWithoutBinding()
}