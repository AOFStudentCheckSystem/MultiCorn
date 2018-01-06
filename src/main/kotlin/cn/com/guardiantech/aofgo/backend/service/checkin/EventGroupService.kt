package cn.com.guardiantech.aofgo.backend.service.checkin

import cn.com.guardiantech.aofgo.backend.data.entity.checkin.ActivityEvent
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.EventGroup
import cn.com.guardiantech.aofgo.backend.repository.checkin.EventGroupRepository
import cn.com.guardiantech.aofgo.backend.repository.checkin.EventRepository
import cn.com.guardiantech.aofgo.backend.request.checkin.CREventToGroupRequest
import cn.com.guardiantech.aofgo.backend.request.checkin.CreateGroupRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by dedztbh on 1/6/18.
 * Project AOFGoBackend
 */

@Service
class EventGroupService @Autowired constructor(
        private val eventRepo: EventRepository,
        private val eventGroupRepo: EventGroupRepository
) {

    /**
     * @throws NoSuchElementException Group not found
     */
    fun createGroup(request: CreateGroupRequest) {
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

}