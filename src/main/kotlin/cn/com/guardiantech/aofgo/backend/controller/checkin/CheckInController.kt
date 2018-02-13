package cn.com.guardiantech.aofgo.backend.controller.checkin

import cn.com.guardiantech.aofgo.backend.data.entity.checkin.ActivityEventRecord
import cn.com.guardiantech.aofgo.backend.exception.BadRequestException
import cn.com.guardiantech.aofgo.backend.exception.EntityNotFoundException
import cn.com.guardiantech.aofgo.backend.request.checkin.CheckInSubmissionRequest
import cn.com.guardiantech.aofgo.backend.request.checkin.CheckInSubmissionResponse
import cn.com.guardiantech.aofgo.backend.service.checkin.CheckInService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * Created by dedztbh on 1/5/18.
 * Project AOFGoBackend
 */

@RestController
@RequestMapping(path = ["/checkin/checkin"])
class CheckInController @Autowired constructor(
        private val checkInService: CheckInService
) {
    private val logger: Logger = LoggerFactory.getLogger(CheckInController::class.java)

    @RequestMapping(path = ["/submit"], method = [RequestMethod.PUT, RequestMethod.PATCH])
    fun checkInSubmission(@RequestBody @Valid request: CheckInSubmissionRequest): CheckInSubmissionResponse = try {
        checkInService.checkInSubmission(request)
    } catch (e: IllegalArgumentException) {
        throw BadRequestException(e.message)
    } catch (e: NoSuchElementException) {
        throw EntityNotFoundException("Event or student not found")
    }

    @RequestMapping(path = ["/record/{eventId}"], method = [RequestMethod.GET])
    fun getRecordForEvent(@PathVariable("eventId") eventId: String): List<ActivityEventRecord> = try {
        checkInService.getRecordForEvent(eventId)
    } catch (e: NoSuchElementException) {
        throw EntityNotFoundException("Event not found")
    }
}