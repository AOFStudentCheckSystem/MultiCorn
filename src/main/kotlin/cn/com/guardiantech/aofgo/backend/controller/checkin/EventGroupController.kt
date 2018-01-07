package cn.com.guardiantech.aofgo.backend.controller.checkin

import cn.com.guardiantech.aofgo.backend.data.entity.checkin.EventGroup
import cn.com.guardiantech.aofgo.backend.request.checkin.CREventToGroupRequest
import cn.com.guardiantech.aofgo.backend.request.checkin.GroupRequest
import cn.com.guardiantech.aofgo.backend.service.checkin.EventGroupService
import javassist.NotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * Created by dedztbh on 1/6/18.
 * Project AOFGoBackend
 */

@RestController
@RequestMapping(path = ["/checkin/event/group"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
class EventGroupController @Autowired constructor(
        private val eventGroupService: EventGroupService
) {
    private val logger: Logger = LoggerFactory.getLogger(EventGroupController::class.java)

    @RequestMapping(path = ["/create"], method = [RequestMethod.POST])
    fun createGroup(@RequestBody @Valid request: GroupRequest) = try {
        eventGroupService.createGroup(request)
    } catch (e: NoSuchElementException) {
        throw NotFoundException("Group not found")
    }

    @RequestMapping(path = ["/edit/{groupId}/add"], method = [RequestMethod.POST])
    fun addEventToGroup(@RequestBody @Valid request: CREventToGroupRequest,
                        @PathVariable("groupId") groupId: Long) = try {
        eventGroupService.addEventToGroup(request, groupId)
    } catch (e: NoSuchElementException) {
        throw NotFoundException("Group or event not found")
    }

    @RequestMapping(path = ["/edit/{groupId}/remove"], method = [RequestMethod.POST])
    fun removeEventFromGroup(@RequestBody @Valid request: CREventToGroupRequest,
                             @PathVariable("groupId") groupId: Long) = try {
        eventGroupService.removeEventFromGroup(request, groupId)
    } catch (e: NoSuchElementException) {
        throw NotFoundException("Group or event not found")
    }

    @RequestMapping(path = ["/edit/{groupId}/set"], method = [RequestMethod.POST])
    fun setEventToGroup(@RequestBody eventIds: Array<Long>,
                        @PathVariable("groupId") groupId: Long) = try {
        eventGroupService.setEventToGroup(eventIds, groupId)
    } catch (e: NoSuchElementException) {
        throw NotFoundException("Group or event not found")
    }

    @RequestMapping(path = ["/edit/{groupId}"], method = [RequestMethod.POST])
    fun updateEventTitle(@RequestBody @Valid request: GroupRequest,
                         @PathVariable("groupId") groupId: Long) = try {
        eventGroupService.updateEventTitle(request, groupId)
    } catch (e: NoSuchElementException) {
        throw NotFoundException("Group not found")
    }


    @RequestMapping(path = ["/remove/{id}"], method = [RequestMethod.DELETE])
    fun removeEventGroupById(@PathVariable id: Long): Long =
            eventGroupService.removeEventGroupById(id)

    @RequestMapping(path = ["/list"])
    fun listEventGroups(pageable: Pageable): Page<EventGroup> =
            eventGroupService.listEventGroups(pageable)

    @GetMapping(path = ["/list/{id}"])
    fun getEventGroupById(@PathVariable id: Long) = try {
        eventGroupService.getEventGroupById(id)
    } catch (e: NoSuchElementException) {
        throw NotFoundException("Group not found")
    }

    @GetMapping(path = ["/listall"])
    fun listAllEventGroups() =
            listEventGroups(
                    PageRequest(0, Int.MAX_VALUE))

    @GetMapping(path = ["/list-available"])
    fun listAvailable() =
            eventGroupService.listAvailable()
}