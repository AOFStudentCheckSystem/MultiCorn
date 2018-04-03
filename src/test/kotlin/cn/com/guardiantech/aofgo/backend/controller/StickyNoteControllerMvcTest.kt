package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.BackendApplication
import cn.com.guardiantech.aofgo.backend.BackendApplicationTestConfiguration
import cn.com.guardiantech.aofgo.backend.data.entity.Notes.StickyNote
import cn.com.guardiantech.aofgo.backend.repository.note.StickyNoteRepository
import org.json.JSONObject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [BackendApplication::class])
@DataJpaTest
@Import(BackendApplicationTestConfiguration::class)
@AutoConfigureMockMvc
@EnableSpringDataWebSupport
class StickyNoteControllerMvcTest {

    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var stickNoteRepo: StickyNoteRepository

    private lateinit var note: StickyNote
    private lateinit var addStickyNoteRawRequest: String
    private lateinit var editStickyNoteRawRequest: String

    @Before
    fun setUp() {
        Assert.assertEquals("Unexpected repository content", 0, stickNoteRepo.count())
        note = stickNoteRepo.save(StickyNote(
                title = "Salty fish",
                text = "What I am"
        )
        )
        Assert.assertEquals("Unexpected repository content", 1, stickNoteRepo.count())

        addStickyNoteRawRequest = """
            {
	        "title": "Another Salty Fish",
	        "text": "Still me",
	        "noteQueryId": ""
            }
        """.trimIndent()

        editStickyNoteRawRequest = """
            {
	        "title": "More Salty Fish",
	        "text": "This is changed",
	        "noteQueryId": "${note.id}"
            }
        """.trimIndent()

    }

    @Test
    fun addNote() {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/notes/add")
                        .content(addStickyNoteRawRequest)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect {
                    var response = JSONObject(it.response.contentAsString)
                    Assert.assertEquals("Another Salty Fish", response.getString("title"))
                    Assert.assertEquals("Still me", response.getString("text"))
                }

        Assert.assertNotNull(stickNoteRepo.count())
        Assert.assertEquals(2, stickNoteRepo.count())
    }

    @Test
    fun deleteNote() {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/notes/${note.id}")
        )
                .andExpect {
                    Assert.assertEquals(HttpStatus.NO_CONTENT.value(), it.response.status)
                }

        Assert.assertNotNull(stickNoteRepo.count())
        Assert.assertEquals(0, stickNoteRepo.count())
    }

    @Test
    fun editNote() {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/notes/edit")
                        .content(editStickyNoteRawRequest)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect {
                    var response = JSONObject(it.response.contentAsString)
                    Assert.assertEquals("More Salty Fish", response.getString("title"))
                    Assert.assertEquals("This is changed", response.getString("text"))
                    Assert.assertEquals(note.id, response.getString("id"))
                }
        Assert.assertNotNull(stickNoteRepo.count())
        Assert.assertEquals(1, stickNoteRepo.count())
    }
}