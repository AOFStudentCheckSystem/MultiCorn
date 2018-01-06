package cn.com.guardiantech.aofgo.backend.controller.checkin

import cn.com.guardiantech.aofgo.backend.request.checkin.CREventToGroupRequest
import cn.com.guardiantech.aofgo.backend.request.checkin.CreateGroupRequest
import cn.com.guardiantech.aofgo.backend.service.checkin.EventGroupService
import javassist.NotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
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
    fun createGroup(@RequestBody @Valid request: CreateGroupRequest) = try {
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

}