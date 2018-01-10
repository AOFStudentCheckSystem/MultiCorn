package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.BackendApplication
import cn.com.guardiantech.aofgo.backend.BackendApplicationTestConfiguration
import cn.com.guardiantech.aofgo.backend.controller.checkin.CheckInController
import cn.com.guardiantech.aofgo.backend.data.entity.Gender
import cn.com.guardiantech.aofgo.backend.data.entity.Student
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.ActivityEvent
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.EventStatus
import cn.com.guardiantech.aofgo.backend.repository.StudentRepository
import cn.com.guardiantech.aofgo.backend.repository.checkin.EventRecordRepository
import cn.com.guardiantech.aofgo.backend.repository.checkin.EventRepository
import cn.com.guardiantech.aofgo.backend.request.checkin.CheckInSubmissionRequest
import cn.com.guardiantech.aofgo.backend.request.checkin.CheckInSubmissionResponse
import cn.com.guardiantech.aofgo.backend.request.checkin.RecordToUpload
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put

/**
 * Created by calvinx on 2018/01/08.
 * Project AOFGoBackend
 */

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [BackendApplication::class])
@DataJpaTest
@Import(BackendApplicationTestConfiguration::class)
@AutoConfigureMockMvc
@EnableSpringDataWebSupport
class CheckInControllerMvcTest {

    @Autowired private lateinit var mockMvc: MockMvc

    @Autowired private lateinit var checkInController: CheckInController
    @Autowired private lateinit var eventRepo: EventRepository
    @Autowired private lateinit var studentRepo: StudentRepository
    @Autowired private lateinit var eventRecordRepository: EventRecordRepository

    private lateinit var event: ActivityEvent
    private lateinit var student: Student
    private lateinit var checkInRawRequest: String

    @Before
    fun setUp() {
        assertEquals("Unexpected repository content", 0, eventRepo.count())
        event = eventRepo.save(ActivityEvent(
                eventName = "Studium",
                eventDescription = "Zeal",
                eventStatus = EventStatus.FUTURE,
                eventTime = Date()
        )
        )
        assertEquals("Unexpected repository content", 1, eventRepo.count())

        assertEquals("Unexpected repository content", 0, studentRepo.count())
        student = studentRepo.save(Student(
                idNumber = "19890535",
                cardSecret = "8988",
                grade = 10,
                dateOfBirth = Date(),
                gender = Gender.MALE,
                dorm = "",
                dormInfo = "",
                account = null
        ))
        assertEquals("Unexpected repository content", 1, studentRepo.count())

        var targetEvent = event.eventId

        checkInRawRequest = """{
        "targetEvent" : "$targetEvent",
        "recordsToUpload" : [
         {
           "timestamp":  1515454572,
           "status": 1,
           "studentId": ${student.idNumber}
         }
         ]
        }""".trimIndent()

        print(checkInRawRequest)
    }

    @Test
    fun addCheckIn() {
        mockMvc.perform(
                put("/checkin/checkin/submit")
                        .content(checkInRawRequest)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect {
                    var response = JSONObject(it.response.contentAsString)
                    assertEquals(response.getString("targetEvent"), event.eventId)
                    //TODO
                }

        assertNotNull(eventRecordRepository.count())
        assertEquals(1, 1)
    }
}