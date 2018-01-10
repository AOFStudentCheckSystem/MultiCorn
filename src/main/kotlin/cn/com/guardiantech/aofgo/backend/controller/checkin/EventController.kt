package cn.com.guardiantech.aofgo.backend.controller.checkin

import cn.com.guardiantech.aofgo.backend.data.entity.checkin.ActivityEvent
import cn.com.guardiantech.aofgo.backend.exception.BadRequestException
import cn.com.guardiantech.aofgo.backend.exception.NotFoundException
import cn.com.guardiantech.aofgo.backend.exception.RepositoryException
import cn.com.guardiantech.aofgo.backend.request.checkin.EventRequest
import cn.com.guardiantech.aofgo.backend.request.checkin.SendEmailRequest
import cn.com.guardiantech.aofgo.backend.service.checkin.EventService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * Created by dedztbh on 1/6/18.
 * Project AOFGoBackend
 */
@RestController
@RequestMapping(path = ["/checkin/event"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
class EventController @Autowired constructor(
        private val eventService: EventService
) {
    private val logger: Logger = LoggerFactory.getLogger(EventController::class.java)

    @RequestMapping(path = ["/{id}"], method = [RequestMethod.DELETE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeEvent(@PathVariable("id") eventID: String) {
        try {
            eventService.removeEvent(eventID)
        } catch (e: NoSuchElementException) {
            throw BadRequestException(e.message)
        }
    }

    @RequestMapping(path = ["/"], method = [RequestMethod.PUT])
    fun createEvent(@RequestBody @Valid request: EventRequest): ActivityEvent =
            eventService.createEvent(request)
//            JSONObject().put("success",true).put("newEvent", JSONObject().put("eventId", evt.eventId)).encode()

    @RequestMapping(path = ["/list"], method = [RequestMethod.GET])
    fun listAllEvents(pageable: Pageable) =
            eventService.listAllEvents(pageable)
//        return eventRepo.findAll(PageRequest(pageable.pageNumber, pageable.pageSize, Sort(Sort.Direction.ASC, "eventTime")))

    @RequestMapping(path = ["/list/{status}"], method = [RequestMethod.GET])
    fun listEventsByStatus(@PathVariable status: String): Set<ActivityEvent> =
            try {
                eventService.listEventsByStatus(status)
            } catch (e: IllegalArgumentException) {
                throw BadRequestException("No event status with name")
            }

    @RequestMapping(path = ["/{id}"], method = [RequestMethod.GET])
    fun getEventById(@PathVariable id: String): ActivityEvent =
            try {
                eventService.getEventById(id)
            } catch (e: NoSuchElementException) {
                throw NotFoundException("Cannot find event")
            }

    @RequestMapping(path = ["/listall"], method = [RequestMethod.GET])
    fun listAllEventsNoPage(): Page<ActivityEvent> =
            eventService.listAllEventsNoPage()

    @RequestMapping(path = ["/{eventId}"], method = [RequestMethod.POST])
    fun editEvent(@PathVariable("eventId") eventId: String,
                  @RequestBody request: EventRequest): ActivityEvent = try {
        eventService.editEvent(eventId, request)
    } catch (e: IllegalArgumentException) {
        throw BadRequestException(e.message)
    } catch (e: NoSuchElementException) {
        throw NotFoundException("Cannot find event")
    }

    //TODO: Judgement and dealing with Student without account
    @RequestMapping(path = ["/sendmail"], method = [RequestMethod.POST])
    fun sendSummaryEmail(@RequestBody @Valid request: SendEmailRequest) = try {
        eventService.sendSummaryEmail(request)
    } catch (e: IllegalArgumentException) {
        throw BadRequestException(e.message)
    } catch (e: NoSuchElementException) {
        throw RepositoryException("Cannot find event")
    }
}