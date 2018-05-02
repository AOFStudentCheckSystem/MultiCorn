package cn.com.guardiantech.aofgo.backend.service.checkin

import abs
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.ActivityEventRecord
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.EventStatus
import cn.com.guardiantech.aofgo.backend.repository.StudentPagedRepository
import cn.com.guardiantech.aofgo.backend.repository.checkin.EventRecordRepository
import cn.com.guardiantech.aofgo.backend.repository.checkin.EventRepository
import cn.com.guardiantech.aofgo.backend.request.checkin.CheckInSubmissionRequest
import cn.com.guardiantech.aofgo.backend.request.checkin.CheckInSubmissionResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import unitDirection

/**
 * Created by dedztbh on 1/6/18.
 * Project AOFGoBackend
 */

@Service
class CheckInService @Autowired constructor(
        private val recordRepository: EventRecordRepository,
        private val eventRepository: EventRepository,
        private val studentPagedRepository: StudentPagedRepository,
        private val eventRecordRepository: EventRecordRepository
) {
    /**
     * @throws IllegalArgumentException
     * @throws NoSuchElementException Event or student not found
     */
    @Transactional
    fun checkInSubmission(request: CheckInSubmissionRequest): CheckInSubmissionResponse {
        val event = eventRepository.findByEventId(request.targetEvent).get()
        if (event.eventStatus.status > 1) {
            throw IllegalArgumentException("Event has been completed. Invalid request.")
        }

        if (event.eventStatus.status < 1) {
            event.eventStatus = EventStatus.BOARDING
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
                targetEventRecord.checkInTime = recordNewStatus.status.unitDirection() * recordTimestamp
            }
            eventRecordRepository.save(targetEventRecord)
        }

        return CheckInSubmissionResponse(
                targetEvent = event.eventId,
                totalRecordsReceived = totalRecords,
                validRecords = validRecords,
                effectiveRecords = effectiveUpdate
        )
    }

    /**
     * @throws NoSuchElementException Event not found
     */
    fun getRecordForEvent(eventId: String) =
            eventRecordRepository.findByEvent(
                    eventRepository.findByEventId(eventId).get())
}