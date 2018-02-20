package cn.com.guardiantech.aofgo.backend.repository.checkin

import cn.com.guardiantech.aofgo.backend.data.entity.Student
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.ActivityEvent
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.ActivityEventRecord
import org.springframework.data.repository.CrudRepository
import java.util.*

/**
 * Created by Codetector on 2017/4/7.
 * Project backend
 */
interface EventRecordRepository : CrudRepository<ActivityEventRecord, Long> {
    fun findByStudent(student: Student): List<ActivityEventRecord>
    fun findByEvent(event: ActivityEvent): List<ActivityEventRecord>
    fun findByEventAndStudent(event: ActivityEvent, student: Student): Optional<ActivityEventRecord>
}