package cn.com.guardiantech.aofgo.backend.controller.checkin

import cn.com.guardiantech.aofgo.backend.data.entity.checkin.ActivityEvent
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.EventStatus
import cn.com.guardiantech.aofgo.backend.exception.RepositoryException
import cn.com.guardiantech.aofgo.backend.repository.checkin.EventRecordRepository
import cn.com.guardiantech.aofgo.backend.repository.checkin.EventRepository
import cn.com.guardiantech.aofgo.backend.request.checkin.EventRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import cn.com.guardiantech.aofgo.backend.service.checkin.EmailService
import cn.com.guardiantech.checkin.server.mail.MailTemplateFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import java.util.*

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
    fun createEvent(@RequestBody @Valid request: EventRequest): ActivityEvent {
        return try {
            eventRepository.save(
                    ActivityEvent(
                            eventName = request.name,
                            eventTime = request.time ?: Date(),
                            eventDescription = request.description ?: ""))
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

    @GetMapping(path = ["/listall"])
    fun listAllEventsNoPage(): Page<ActivityEvent> = listAllEvents(PageRequest(0, Int.MAX_VALUE))

    @RequestMapping(path = ["/edit/{eventId}"], method = [(RequestMethod.POST)])
    fun editEvent(@PathVariable("eventId") eventId: String,
                  @RequestBody @Valid request: EventRequest): ActivityEvent {
        val eventToEdit = eventRepository.findByEventId(eventId).get()
        if (eventToEdit.eventStatus == EventStatus.COMPLETED) {
            throw IllegalArgumentException("Action Edit is not allowed on completed events")
        }
        if (request.time != null) {
            eventToEdit.eventTime = request.time
        }
        if (request.name.isNotEmpty()) {
            eventToEdit.eventName = request.name
        }
        if (request.description != null) {
            eventToEdit.eventDescription = request.description
        }
        if (request.status != null) {
            eventToEdit.eventStatus = request.status
        }
        return eventRepository.save(eventToEdit)
    }

    //TODO: Add Email API
//    @RequestMapping(path = ["/sendmail"], method = [(RequestMethod.POST)])
//    fun sendSummaryEmail(@RequestParam("eventId") eventId: String,
//                         @RequestParam("address") addr: String,
//                         @AuthenticationPrincipal auth: Token): ResponseEntity<String> {
//        if (!isValidEmailAddress(addr))
//            throw IllegalArgumentException("Invalid email address")
//        val event = eventRepo.findByEventId(eventId).get()
//        if (event.eventStatus < 2 && !auth.isAuthenticated(Permission.ADMIN)) {
//            throw IllegalArgumentException("Only completed events is allowed to compile a result list")
//        }
//        val template = MailTemplateFactory.createTemplateByFileName("eventNotify")
//
//        val emailContent = ArrayList<String>()
//        eventRecordRepo.findByEvent(event).filter { it.checkInTime > 0 }.sortedWith(kotlin.Comparator { o1, o2 ->
//            o1.student.lastName.compareTo(o2.student.lastName, true)
//        }).forEach {
//            val sb = StringBuilder()
//            sb.append(it.student.preferredName)
//                    .append(" ")
//                    .append(it.student.lastName)
//                    .append(" - ")
//                    .append(it.student.dorm)
//            emailContent.add(sb.toString())
//        }
//        template.setStringValue("eventName", event.eventName)
//        template.setStringValue("count", emailContent.size)
//        template.setListValue("studentList", emailContent)
//        template.setStringValue("eventId", event.eventId)
//        Thread(Runnable {
//            mailService.sendMailWithTitle(template, event.eventName, addr)
//        }).start()
//
//        return ActionResult(true).encode()
//    }
}