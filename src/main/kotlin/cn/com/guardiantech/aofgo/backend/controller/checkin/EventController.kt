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
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * Created by dedztbh on 1/6/18.
 * Project AOFGoBackend
 */
@RestController
@RequestMapping(path = ["checkin/event"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
class EventController @Autowired constructor(
        private val eventService: EventService
) {
    private val logger: Logger = LoggerFactory.getLogger(EventController::class.java)

    @RequestMapping(path = ["/remove/{id}"], method = [RequestMethod.DELETE])
    fun removeEvent(@PathVariable("id") eventID: String) =
            eventService.removeEvent(eventID)

    @RequestMapping(path = ["/create"], method = [RequestMethod.POST])
    fun createEvent(@RequestBody @Valid request: EventRequest): ActivityEvent =
            try {
                eventService.createEvent(request)
//            JSONObject().put("success",true).put("newEvent", JSONObject().put("eventId", evt.eventId)).encode()
            } catch (e: Throwable) {
                logger.error("Error @ createEvent", e)
                throw RepositoryException(e.message)
            }

    @RequestMapping(path = ["/list"])
    fun listAllEvents(pageable: Pageable) =
            eventService.listAllEvents(pageable)
//        return eventRepo.findAll(PageRequest(pageable.pageNumber, pageable.pageSize, Sort(Sort.Direction.ASC, "eventTime")))

    @RequestMapping(path = ["/list/{status}"])
    fun listEventsByStatus(@PathVariable status: String): Set<ActivityEvent> =
            try {
                eventService.listEventsByStatus(status)
            } catch (e: Throwable) {
                logger.error("Error @ listEventsByStatus", e)
                throw BadRequestException(e.message)
            }

    @RequestMapping(path = ["/list/{id}"], method = [(RequestMethod.GET)])
    fun getEventById(@PathVariable id: String): ActivityEvent =
            try {
                eventService.getEventById(id)
            } catch (e: NoSuchElementException) {
                logger.error("Error @ createEvent", e)
                throw NotFoundException(e.message)
            }

    @GetMapping(path = ["/listall"])
    fun listAllEventsNoPage(): Page<ActivityEvent> =
            eventService.listAllEventsNoPage()

    @RequestMapping(path = ["/edit/{eventId}"], method = [(RequestMethod.POST)])
    fun editEvent(@PathVariable("eventId") eventId: String,
                  @RequestBody @Valid request: EventRequest): ActivityEvent =
            try {
                eventService.editEvent(eventId, request)
            } catch (e: IllegalArgumentException) {
                logger.error("Error @ editEvent", e)
                throw BadRequestException(e.message)
            } catch (e: Throwable) {
                logger.error("Error @ editEvent", e)
                throw RepositoryException(e.message)
            }

    //TODO: Judgement and dealing with Student without account
    @RequestMapping(path = ["/sendmail"], method = [(RequestMethod.POST)])
    fun sendSummaryEmail(@RequestBody @Valid request: SendEmailRequest) =
            try {
                eventService.sendSummaryEmail(request)
            } catch (e: IllegalArgumentException) {
                logger.error("Error @ sendSummaryEmail", e)
                throw BadRequestException(e.message)
            } catch (e: NoSuchElementException) {
                logger.error("Error @ sendSummaryEmail", e)
                throw NotFoundException(e.message)
            } catch (e: Throwable) {
                logger.error("Error @ sendSummaryEmail", e)
                throw RepositoryException(e.message)
            }
}