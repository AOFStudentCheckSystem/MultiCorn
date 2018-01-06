package cn.com.guardiantech.aofgo.backend.controller.checkin

import cn.com.guardiantech.aofgo.backend.data.entity.checkin.ActivityEvent
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.EventStatus
import cn.com.guardiantech.aofgo.backend.exception.RepositoryException
import cn.com.guardiantech.aofgo.backend.repository.checkin.EventRecordRepository
import cn.com.guardiantech.aofgo.backend.repository.checkin.EventRepository
import cn.com.guardiantech.aofgo.backend.request.checkin.CreateEventRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import cn.com.guardiantech.aofgo.backend.service.checkin.EmailService

/**
 * Created by dedztbh on 1/6/18.
 * Project AOFGoBackend
 */
@RestController
@RequestMapping(path = ["checkin/event"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
class EventController @Autowired constructor(
        private val eventRepository: EventRepository,
        private val eventRecordRepository: EventRecordRepository,
        private val mailService: EmailService
) {
    @RequestMapping(path = ["/remove/{id}"], method = [RequestMethod.DELETE])
    fun removeEvent(@PathVariable("id") eventID: String) =
            eventRepository.removeByEventId(eventID)

    @RequestMapping(path = ["/create"], method = [RequestMethod.POST])
    fun createEvent(@RequestBody @Valid request: CreateEventRequest): ActivityEvent {
        return try {
            eventRepository.save(ActivityEvent(eventName = request.name, eventTime = request.time, eventDescription = request.description))
//            JSONObject().put("success",true).put("newEvent", JSONObject().put("eventId", evt.eventId)).encode()
        } catch (e: Throwable) {
            throw RepositoryException()
        }
    }

    @RequestMapping(path = ["/list"])
    fun listAllEvents(pageable: Pageable) =
            eventRepository.findAll(pageable)
//        return eventRepo.findAll(PageRequest(pageable.pageNumber, pageable.pageSize, Sort(Sort.Direction.ASC, "eventTime")))

    @RequestMapping(path = ["/list/{status}"])
    fun listEventsByStatus(@PathVariable status: String): Set<ActivityEvent> {
        val statusNumber = status.toIntOrNull()
        val statusEnum = if (statusNumber != null) {
            EventStatus.values().first {
                it.status == statusNumber
            }
        } else {
            EventStatus.valueOf(status)
        }
        return eventRepository.findByEventStatus(statusEnum)
    }

    @RequestMapping(path = ["/list/{id}"], method = [(RequestMethod.GET)])
    fun getEventById(@PathVariable id: String) =
            eventRepository.findByEventId(id).get()

    //TODO: listall and on APIs
}