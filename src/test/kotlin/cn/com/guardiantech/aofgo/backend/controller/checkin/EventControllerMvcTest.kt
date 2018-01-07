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
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [BackendApplication::class])
@DataJpaTest
@Import(BackendApplicationTestConfiguration::class)
@AutoConfigureMockMvc
class EventControllerMvcTest {

    @Autowired private lateinit var mockMvc: MockMvc

    @Autowired private lateinit var eventController: EventController
    @Autowired private lateinit var eventRepo: EventRepository

    private lateinit var event: ActivityEvent


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
                        .content("""
                            {
                                "name":"Tset",
                                "description":null,
                                "time":613006413
                            }
                        """.trimIndent())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect{
                    val obj = JSONObject(it.response.contentAsString)
                    assertNotNull(obj.optString("eventId", null))
                    assertEquals( "Tset", obj.optString("eventName", null))
                    assertEquals("", obj.optString("eventDescription", null))
                    assertEquals(89, Date(obj.optLong("eventTime", Long.MIN_VALUE) * 1000).year)
                    assertEquals("FUTURE", obj.optString("eventStatus"))
                }
        assertEquals(count + 1, eventRepo.count())
    }

    @Test
    fun listAllEvents() {
//        mockMvc
//                .perform(get())
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