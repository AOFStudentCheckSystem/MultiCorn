package cn.com.guardiantech.aofgo.backend.service.checkin

import abs
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.ActivityEventRecord
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.EventStatus
import cn.com.guardiantech.aofgo.backend.repository.checkin.EventRecordRepository
import cn.com.guardiantech.aofgo.backend.repository.checkin.EventRepository
import cn.com.guardiantech.aofgo.backend.repository.checkin.StudentPagedRepository
import cn.com.guardiantech.aofgo.backend.request.checkin.CheckInSubmissionRequest
import cn.com.guardiantech.aofgo.backend.request.checkin.CheckInSubmissionResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import unitDirection

/**
 * Created by dedztbh on 1/6/18.
 * Project AOFGoBackend
 */

@Service
class EventService @Autowired constructor(
        private val recordRepository: EventRecordRepository,
        private val eventRepository: EventRepository,
        private val studentPagedRepository: StudentPagedRepository,
        private val eventRecordRepository: EventRecordRepository
) {
}