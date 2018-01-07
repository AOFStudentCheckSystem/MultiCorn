package cn.com.guardiantech.aofgo.backend.controller.checkin

import cn.com.guardiantech.aofgo.backend.BackendApplication
import cn.com.guardiantech.aofgo.backend.BackendApplicationTestConfiguration
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.ActivityEvent
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.EventStatus
import cn.com.guardiantech.aofgo.backend.repository.checkin.ActivityEventRepository
import cn.com.guardiantech.aofgo.backend.request.checkin.EventRequest
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
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
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
    @Autowired private lateinit var eventRepo: ActivityEventRepository

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
    }

    @Test
    fun listAllEvents() {
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