package cn.com.guardiantech.aofgo.backend.controller.checkin

import abs
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.ActivityEventRecord
import cn.com.guardiantech.aofgo.backend.repository.checkin.EventRecordRepository
import cn.com.guardiantech.aofgo.backend.repository.checkin.EventRepository
import cn.com.guardiantech.aofgo.backend.repository.checkin.StudentPagedRepository
import cn.com.guardiantech.aofgo.backend.request.checkin.CheckInSubmissionRequest
import cn.com.guardiantech.aofgo.backend.request.checkin.CheckInSubmissionResponse
import cn.com.guardiantech.aofgo.backend.request.checkin.RecordForEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import unitDirection
import javax.validation.Valid

/**
 * Created by dedztbh on 1/5/18.
 * Project AOFGoBackend
 */

@RestController
@RequestMapping(path = ["/checkin"])
class CheckInController @Autowired constructor(
        private val recordRepository: EventRecordRepository,
        private val eventRepository: EventRepository,
        private val studentPagedRepository: StudentPagedRepository,
        private val eventRecordRepository: EventRecordRepository
) {
    @RequestMapping(path = ["/submit"], method = [RequestMethod.PUT, RequestMethod.PATCH])
    fun checkInSubmission(@RequestBody @Valid request: CheckInSubmissionRequest): CheckInSubmissionResponse {
//        var isRequestValid = true
//        var error: Throwable? = null
        try {
            val event = eventRepository.findByEventId(request.targetEvent).get()
            if (event.eventStatus > 1) {
                throw IllegalArgumentException("Event has been completed. Invalid request.")
            }

            if (event.eventStatus < 1) {
                event.eventStatus = 1
                eventRepository.save(event)
            }

            val records = request.recordsToUpload

            var effectiveUpdate = 0
            var validRecords = 0
            val totalRecords = records.size

            records.forEach { o ->
                validRecords++
                val recordTimestamp = o.timestamp
                val recordNewStatus = o.status
                val targetStudent = studentPagedRepository.findByIdNumberIgnoreCase(o.studentId).get()
                val targetEventRecord = recordRepository.findByEventAndStudent(event, targetStudent).orElseGet {
                    val r = ActivityEventRecord()
                    r.event = event
                    r.student = targetStudent
                    r
                }
                if (targetEventRecord.checkInTime.abs() <= recordTimestamp.abs()) {
                    effectiveUpdate++
                    targetEventRecord.checkInTime = recordNewStatus.unitDirection() * recordTimestamp
                }
                eventRecordRepository.save(targetEventRecord)
            }

            return CheckInSubmissionResponse(
                    targetEvent = event.eventId,
                    totalRecordsReceived = totalRecords,
                    validRecords = validRecords,
                    effectiveRecords = effectiveUpdate
            )
        } catch (e: Throwable) {
//            isRequestValid = false
//            error = e
            throw e
        }
    }

    @RequestMapping(path = ["/record/{eventId}"], method = [(RequestMethod.GET)])
    fun getRecordForEvent(@PathVariable("eventId") eventId: String): RecordForEvent = RecordForEvent(
            eventRecordRepository.findByEvent(
                    eventRepository.findByEventId(eventId).get()).toTypedArray())
}