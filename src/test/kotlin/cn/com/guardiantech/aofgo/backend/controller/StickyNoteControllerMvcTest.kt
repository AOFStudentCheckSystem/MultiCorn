package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.BackendApplication
import cn.com.guardiantech.aofgo.backend.BackendApplicationTestConfiguration
import cn.com.guardiantech.aofgo.backend.data.entity.Notes.StickyNote
import cn.com.guardiantech.aofgo.backend.repository.note.StickyNoteRepository
import org.junit.Assert
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc

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
	        "title": "testNote",
	        "text": "ajfksd",
	        "noteQueryId": ""
            }
        """.trimIndent()


    }

}