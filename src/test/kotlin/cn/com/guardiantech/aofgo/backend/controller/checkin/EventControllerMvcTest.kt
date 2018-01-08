package cn.com.guardiantech.aofgo.backend.controller.checkin

import cn.com.guardiantech.aofgo.backend.BackendApplication
import cn.com.guardiantech.aofgo.backend.BackendApplicationTestConfiguration
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.ActivityEvent
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.EventStatus
import cn.com.guardiantech.aofgo.backend.repository.checkin.EventRepository
import cn.com.guardiantech.aofgo.backend.request.checkin.EventRequest
import org.json.JSONObject
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.ISO8601DateFormat
import java.text.DateFormat


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [BackendApplication::class])
@DataJpaTest
@Import(BackendApplicationTestConfiguration::class)
@AutoConfigureMockMvc
@EnableSpringDataWebSupport
class EventControllerMvcTest {

    @Autowired private lateinit var mockMvc: MockMvc

    @Autowired private lateinit var eventController: EventController
    @Autowired private lateinit var eventRepo: EventRepository

    private lateinit var event: ActivityEvent

    private val eventCreationRawRequest = """
                            {
                                "name":"Tset",
                                "description":null,
                                "time":613006413
                            }
                        """.trimIndent()

    @Before
    fun setUp() {
        event = eventController.createEvent(
                EventRequest(
                        name = "まほう",
                        description = "Mahou",
                        status = EventStatus.FUTURE,
                        time = Date()
                )
        )
        assertNotNull(event)
        assertNotNull(event.id)
    }

    @Test
    fun removeEvent() {
        val count = eventRepo.count()
        assert(count > 0)
        mockMvc
                .perform(delete("/checkin/event/remove/${event.eventId}"))
                .andExpect(MockMvcResultMatchers.status().isNoContent)
        assertEquals(count - 1, eventRepo.count())
    }

    @Test
    fun testRemoveEventFail() {
        val count = eventRepo.count()
        assert(count > 0)
        mockMvc
                .perform(delete("/checkin/event/remove/-1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
        assertEquals(count, eventRepo.count())
    }

    @Test
    fun createEvent() {
        val count = eventRepo.count()
        mockMvc
                .perform(post("/checkin/event/create")
                        .content(eventCreationRawRequest)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect {
                    val obj = JSONObject(it.response.contentAsString)
                    assertEquals("Tset", obj.optString("eventName", null))
                    val id= obj.optString("eventId", null)
                    assertNotNull(id)
                    assertEquals("", obj.optString("eventDescription", null))
                    assertEquals(613006413000, eventRepo.findByEventId(id).get().eventTime.time)
                    assertEquals(613006413, obj.optLong("eventTime", Long.MIN_VALUE))
                    assertEquals("FUTURE", obj.optString("eventStatus"))
                }
        assertEquals(count + 1, eventRepo.count())
    }


    @Test
    fun listAllEvents() {
        mockMvc
                .perform(get("/checkin/event/list")
                        .content(eventCreationRawRequest)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo {
                    println(it.response.contentAsString)
                }
    }

    @Test
    fun listEventsByStatus() {
    }

    @Test
    fun getEventById() {
    }

    @Test
    fun listAllEventsNoPage() {
    }

    @Test
    fun editEvent() {
    }

    @Test
    fun sendSummaryEmail() {
    }
}