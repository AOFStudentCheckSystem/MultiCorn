package cn.com.guardiantech.aofgo.backend.controller.checkin

import cn.com.guardiantech.aofgo.backend.repository.checkin.EventRecordRepository
import cn.com.guardiantech.aofgo.backend.repository.checkin.EventRepository
import cn.com.guardiantech.aofgo.backend.repository.checkin.StudentPagedRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

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
){
//    @RequestMapping(path = ["/submit"], method = [RequestMethod.PUT, RequestMethod.PATCH])

}