package cn.com.guardiantech.aofgo.backend.service.checkin

import cn.com.guardiantech.aofgo.backend.data.entity.checkin.ActivityEvent
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.EventGroup
import cn.com.guardiantech.aofgo.backend.repository.checkin.EventGroupRepository
import cn.com.guardiantech.aofgo.backend.repository.checkin.EventRepository
import cn.com.guardiantech.aofgo.backend.request.checkin.CREventToGroupRequest
import cn.com.guardiantech.aofgo.backend.request.checkin.CreateGroupRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

/**
 * Created by dedztbh on 1/6/18.
 * Project AOFGoBackend
 */

@Service
class EventGroupService @Autowired constructor(
        private val eventRepo: EventRepository,
        private val eventGroupRepo: EventGroupRepository
) {

    fun createGroup(request: CreateGroupRequest) {
        val events: MutableSet<ActivityEvent> = hashSetOf()
        request.groupItems.forEach {
            //NSEE
            events.add(eventRepo.findByEventId(it).get())
        }
        val group = eventGroupRepo.save(EventGroup(name = request.name, events = events))
    }

    //NSEE
    fun addEventToGroup(request: CREventToGroupRequest, groupId: Long) {
        val targetGroup = eventGroupRepo.findById(groupId).get()
        val targetEvent = eventRepo.findByEventId(request.eventId).get()
        targetGroup.events.add(targetEvent)
        eventGroupRepo.save(targetGroup)
    }

}