package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.BackendApplication
import cn.com.guardiantech.aofgo.backend.BackendApplicationTestConfiguration
import org.junit.Assert.assertNotNull
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [BackendApplication::class])
@DataJpaTest
@Import(BackendApplicationTestConfiguration::class)
@AutoConfigureMockMvc
class AuthenticationControllerMvcTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun mockMvcInitialize() {
        assertNotNull(mockMvc)
    }

    @Test
    fun register() {
        val registerAdmin = """
            {
               "principal": {
                   "type": "USERNAME",
                   "identification": "admin"
               },
               "credential": {
                   "type": "PASSWORD",
                   "secret": "123456"
               },
               "subjectAttachedInfo": ""
            }
        """.trimIndent()

        mockMvc
                .perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(registerAdmin)
                )
                .andDo(print())
                .andExpect(status().isOk)

    }

    @Test
    fun authenticate() {
    }
}