package cn.com.guardiantech.aofgo.backend.service.checkin

import cn.com.guardiantech.aofgo.backend.data.entity.checkin.ActivityEvent
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.EventStatus
import cn.com.guardiantech.aofgo.backend.repository.checkin.EventRecordRepository
import cn.com.guardiantech.aofgo.backend.repository.checkin.EventRepository
import cn.com.guardiantech.aofgo.backend.request.checkin.EventRequest
import cn.com.guardiantech.aofgo.backend.request.checkin.SendEmailRequest
import cn.com.guardiantech.aofgo.backend.service.checkin.mail.MailTemplateFactory
import cn.com.guardiantech.aofgo.backend.util.isValidEmailAddress
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

/**
 * Created by dedztbh on 1/6/18.
 * Project AOFGoBackend
 */

@Service
class EventService @Autowired constructor(
        private val eventRepository: EventRepository,
        private val eventRecordRepository: EventRecordRepository,
        private val mailService: EmailService
) {

    fun removeEvent(eventID: String) = eventRepository.removeByEventId(eventID)

    fun createEvent(request: EventRequest): ActivityEvent {
        return eventRepository.save(
                ActivityEvent(
                        eventName = request.name,
                        eventTime = request.time ?: Date(),
                        eventDescription = request.description ?: ""))
    }

    fun listAllEvents(pageable: Pageable): Page<ActivityEvent> =
            eventRepository.findAll(pageable)

    /**
     * @throws IllegalArgumentException No event status with name
     */
    fun listEventsByStatus(status: String): Set<ActivityEvent> {
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

    /**
     * @throws NoSuchElementException Cannot find event
     */
    fun getEventById(id: String) = eventRepository.findByEventId(id).get()

    fun listAllEventsNoPage(): Page<ActivityEvent> =
            listAllEvents(PageRequest(0, Int.MAX_VALUE))

    /**
     * @throws NoSuchElementException Cannot find event
     * @throws IllegalArgumentException
     */
    fun editEvent(eventId: String, request: EventRequest): ActivityEvent {
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

    /**
     * @throws NoSuchElementException Cannot find event
     * @throws IllegalArgumentException
     */
    fun sendSummaryEmail(request: SendEmailRequest) {
        if (!isValidEmailAddress(request.address)) {
            throw IllegalArgumentException("Invalid email address")
        }
        val event = eventRepository.findByEventId(request.eventId).get()
        if (event.eventStatus.status < 2) {
            throw IllegalArgumentException("Only completed events is allowed to compile a result list")
        }
        val template = MailTemplateFactory.createTemplateByFileName("eventNotify")

        val emailContent = ArrayList<String>()
        eventRecordRepository.findByEvent(event).filter { it.checkInTime > 0 }.sortedWith(kotlin.Comparator { o1, o2 ->
            if (o1.student.account != null && o2.student.account != null) {
                o1.student.account!!.lastName.compareTo(o2.student.account!!.lastName, true)
            }
            TODO("Student without account?")
        }).forEach {
            val sb = StringBuilder()
            sb.append(it.student.account!!.preferredName)
                    .append(" ")
                    .append(it.student.account!!.lastName)
                    .append(" - ")
                    .append(it.student.dorm)
            emailContent.add(sb.toString())
        }
        template.setStringValue("eventName", event.eventName)
        template.setStringValue("count", emailContent.size)
        template.setListValue("studentList", emailContent)
        template.setStringValue("eventId", event.eventId)
        Thread(Runnable {
            mailService.sendMailWithTitle(template, event.eventName, request.address)
        }).start()
    }
}