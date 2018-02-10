package cn.com.guardiantech.aofgo.backend.controller.checkin

import cn.com.guardiantech.aofgo.backend.BackendApplication
import cn.com.guardiantech.aofgo.backend.BackendApplicationTestConfiguration
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.ActivityEvent
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.EventStatus
import cn.com.guardiantech.aofgo.backend.repository.checkin.EventRepository
import cn.com.guardiantech.aofgo.backend.request.checkin.EventRequest
import cn.com.guardiantech.aofgo.backend.test.authutil.AuthenticationUtil
import org.json.JSONArray
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


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [BackendApplication::class])
@DataJpaTest
@Import(BackendApplicationTestConfiguration::class)
@AutoConfigureMockMvc
@EnableSpringDataWebSupport
class EventControllerMvcTest {

    @Autowired private lateinit var mockMvc: MockMvc

    @Autowired private lateinit var eventRepo: EventRepository

    private lateinit var event: ActivityEvent

    private val eventCreationRawRequest = """
                            {
                                "name":"Tset",
                                "description":null,
                                "time":613006413
                            }
                        """.trimIndent()

    @Autowired
    private lateinit var authenticationUtil: AuthenticationUtil

    @Before
    fun setUp() {
        event = eventRepo.save(
                ActivityEvent(
                        eventName = "まほう",
                        eventDescription = "Mahou",
                        eventStatus = EventStatus.FUTURE,
                        eventTime = Date()
                )
        )
        assertNotNull(event)
        assertNotNull(event.id)
        assertEquals("Repository is not properly initialized", 1, eventRepo.count())
        authenticationUtil.prepare()
    }

    @Test
    fun removeEvent() {
        val count = eventRepo.count()
        assert(count > 0)
        mockMvc
                .perform(delete("/checkin/event/${event.eventId}")
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                }))
                .andExpect(MockMvcResultMatchers.status().isNoContent)
        assertEquals(count - 1, eventRepo.count())
    }

    @Test
    fun testRemoveEventFail() {
        val count = eventRepo.count()
        assert(count > 0)
        mockMvc
                .perform(delete("/checkin/event/-1")
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                }))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
        assertEquals(count, eventRepo.count())
    }

    @Test
    fun createEvent() {
        val count = eventRepo.count()

        mockMvc.perform(put("/checkin/event/")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                })
        )
                .andDo {
                    println("AAA: ${it.response.contentAsString}")
                }
                .andExpect(MockMvcResultMatchers.status().isBadRequest)

        mockMvc
                .perform(put("/checkin/event/")
                        .content(eventCreationRawRequest)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .with({
                            it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                            it
                        })
                )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect {
                    val obj = JSONObject(it.response.contentAsString)
                    assertEquals("Tset", obj.optString("eventName", null))
                    val id = obj.optString("eventId", null)
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
                        .with({
                            it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                            it
                        })
                )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo {
                    println("Content: ${it.response.contentAsString}")
                }
    }

    @Test
    fun listEventsByStatus() {
        val boardingEvent = eventRepo.save(
                ActivityEvent(
                        eventName = "まほう2",
                        eventStatus = EventStatus.BOARDING
                )
        )
        mockMvc.perform(get("/checkin/event/list/future")
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                }))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo {
                    val arr = JSONArray(it.response.contentAsString)
                    assertEquals(1, arr.length())
                    assertNotEquals(boardingEvent.eventId, arr.getJSONObject(0).getString("eventId"))
                }
        mockMvc.perform(get("/checkin/event/list/1")
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                }))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo {
                    val arr = JSONArray(it.response.contentAsString)
                    assertEquals(1, arr.length())
                    assertEquals(boardingEvent.eventId, arr.getJSONObject(0).getString("eventId"))
                }
        mockMvc.perform(get("/checkin/event/list/COMPLETED")
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                }))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo {
                    val arr = JSONArray(it.response.contentAsString)
                    assertEquals(0, arr.length())
                }
        mockMvc.perform(get("/checkin/event/list/MAHOU")
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                }))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun getEventById() {
        val boardingEvent = eventRepo.save(
                ActivityEvent(
                        eventName = "まほう2",
                        eventStatus = EventStatus.BOARDING
                )
        )
        mockMvc.perform(get("/checkin/event/${boardingEvent.eventId}")
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                }))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo { assertEquals(-1L, JSONObject(it.response.contentAsString).optLong("id", -1L)) }
        mockMvc.perform(get("/checkin/event/DNE")
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                }))
                .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun listAllEventsNoPage() {
        mockMvc.perform(get("/checkin/event/listall")
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                }))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo {
                    val contents = JSONArray(it.response.contentAsString)
                    assertEquals(1, contents.length())
                }
    }

    @Test
    fun editEvent() {
        var boardingEvent = eventRepo.save(
                ActivityEvent(
                        eventName = "まほう2",
                        eventDescription = "Test1",
                        eventTime = Date(0L),
                        eventStatus = EventStatus.BOARDING
                )
        )
        mockMvc.perform(post("/checkin/event/")
                .content(
                        """
                            {
                                "eventId": "${boardingEvent.eventId}",
                                "name": "233",
                                "description": "Tset2",
                                "time": 1,
                                "status": "FUTURE"
                            }
                        """.trimIndent()
                )
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                }))
                .andExpect(MockMvcResultMatchers.status().isOk)
        boardingEvent = eventRepo.findByEventId(boardingEvent.eventId).get()
        assertEquals("233", boardingEvent.eventName)
        assertEquals("Tset2", boardingEvent.eventDescription)
        assertEquals(1000, boardingEvent.eventTime.time)
        assertEquals(EventStatus.FUTURE, boardingEvent.eventStatus)

        mockMvc.perform(post("/checkin/event/")
                .content(
                        """
                            {
                                "eventId": "${boardingEvent.eventId}",
                                "description": "",
                                "status": "COMPLETED"
                            }
                        """.trimIndent()
                )
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                }))
                .andExpect(MockMvcResultMatchers.status().isOk)
        boardingEvent = eventRepo.findByEventId(boardingEvent.eventId).get()
        assertEquals("233", boardingEvent.eventName)
        assertEquals("", boardingEvent.eventDescription)
        assertEquals(1000, boardingEvent.eventTime.time)
        assertEquals(EventStatus.COMPLETED, boardingEvent.eventStatus)

        mockMvc.perform(post("/checkin/event/")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                }))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)

        mockMvc.perform(post("/checkin/event/")
                .content("{\"eventId\": \"${boardingEvent.eventId}\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                }))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)

        mockMvc.perform(post("/checkin/event/")
                .content("{\"eventId\": \"DNE\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                }))
                .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun sendSummaryEmail() {
        // Cannot test
    }
}