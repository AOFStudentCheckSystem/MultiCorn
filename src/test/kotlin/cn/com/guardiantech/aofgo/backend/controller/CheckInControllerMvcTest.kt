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
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

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

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var checkInController: CheckInController
    @Autowired
    private lateinit var eventRepo: EventRepository
    @Autowired
    private lateinit var studentRepo: StudentRepository
    @Autowired
    private lateinit var eventRecordRepository: EventRecordRepository

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

        checkInRawRequest = """{
        "targetEvent" : "${event.eventId}",
        "recordsToUpload" : [
         {
           "timestamp":  1518371246,
           "status": 1,
           "studentId": ${student.idNumber}
         }
         ]
        }""".trimIndent()
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
                    assertEquals(response.getInt("totalRecordsReceived"), 1)
                    assertEquals(response.getInt("validRecords"), 1)
                    assertEquals(response.getInt("effectiveRecords"), 1)
                }

        assertNotNull(eventRecordRepository.count())
        assertEquals(1, eventRecordRepository.count())

        val record = eventRecordRepository.findByEvent(event)[0]
        assertEquals(1518371246, record.checkInTime)
    }

    @Test
    fun addCheckInWithDefaultStatus() {

        checkInRawRequest = """{
        "targetEvent" : "${event.eventId}",
        "recordsToUpload" : [
         {
           "timestamp":  1518371246,
           "studentId": ${student.idNumber}
         }
         ]
        }""".trimIndent()

        mockMvc.perform(
                put("/checkin/checkin/submit")
                        .content(checkInRawRequest)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect {
                    var response = JSONObject(it.response.contentAsString)
                    assertEquals(response.getString("targetEvent"), event.eventId)
                    assertEquals(response.getInt("totalRecordsReceived"), 1)
                    assertEquals(response.getInt("validRecords"), 1)
                    assertEquals(response.getInt("effectiveRecords"), 1)
                }

        assertNotNull(eventRecordRepository.count())
        assertEquals(1, eventRecordRepository.count())

        val record = eventRecordRepository.findByEvent(event)[0]
        assertEquals(1518371246, record.checkInTime)
    }

    @Test
    fun retrieveRecord() {
        mockMvc.perform(
                put("/checkin/checkin/submit/")
                        .content(checkInRawRequest)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andDo {
                    var response1 = JSONObject(it.response.contentAsString)
                    var eventId = response1.getString("targetEvent")
                    mockMvc.perform(
                            get("/checkin/checkin/record/${eventId}")
                    )
                            .andExpect {
                                var response = JSONArray(it.response.contentAsString)
                                var allTheShit = response.getJSONObject(0)
                                var studentResponse = allTheShit.getJSONObject("student")
//                                var eventResponse = allTheShit.getJSONObject("event")
                                assertEquals(studentResponse.getString("idNumber"), student.idNumber)
                                assertEquals(studentResponse.getInt("grade"), student.grade)
//                                assertEquals(eventResponse.getString("eventName"), event.eventName)
                            }
                }
    }

    @Test
    fun checkInPastEvent() {
        event.eventStatus = EventStatus.COMPLETED

        mockMvc.perform(
                put("/checkin/checkin/submit")
                        .content(checkInRawRequest)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    fun stupidRequest() {
        mockMvc.perform(
                put("/checkin/checkin/submit")
                        .content("I am not JSON")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}