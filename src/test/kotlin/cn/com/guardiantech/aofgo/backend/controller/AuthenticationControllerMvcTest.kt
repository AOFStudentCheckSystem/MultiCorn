package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.BackendApplication
import cn.com.guardiantech.aofgo.backend.BackendApplicationTestConfiguration
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.RequestPostProcessor
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [BackendApplication::class])
@DataJpaTest
@Import(BackendApplicationTestConfiguration::class)
@AutoConfigureMockMvc
class AuthenticationControllerMvcTest {

    @Autowired private lateinit var mockMvc: MockMvc

    @Test
    fun mockMvcInitialize() {
        assertNotNull(mockMvc)
    }

    private val adminAuthenticationRequestJson = """
            {
               "principal": {
                   "type": "USERNAME",
                   "identification": "admin"
               },
               "credential": {
                   "type": "PASSWORD",
                   "secret": "123456"
               }
            }
        """.trimIndent()

    @Test
    @DirtiesContext
    fun register() {
        val postRequest = post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.adminAuthenticationRequestJson)

        // Test Register -- Should expect a 200
        mockMvc
                .perform(postRequest)
                .andExpect(status().isOk)
                .andExpect {
                    val content = it.response.contentAsString
                    assertNotNull(content)
                    assert(content.isNotEmpty())
                    assertNotNull(JsonFactory().createParser(content))
                }

        mockMvc.perform(postRequest)
                .andExpect(status().isBadRequest)

    }

    @Test
    @DirtiesContext
    fun authenticate() {

        var session = ""

        mockMvc
                .perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(this.adminAuthenticationRequestJson)
                )
                .andExpect(status().isOk)

        mockMvc
                .perform(
                        post("/auth/auth")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(this.adminAuthenticationRequestJson)
                )
                .andExpect(status().isOk)
                .andDo {
                    val rootNode = ObjectMapper().readTree(it.response.contentAsString)
                    session = rootNode.get("sessionKey").textValue()
                }

        assertNotEquals("", session) // Session Exists

        //Test Authentication with BOTH Session and Body (Session should NOT update)

        mockMvc
                .perform(
                        post("/auth/auth")
                                .with(RequestPostProcessor {
                                    it.addHeader("Authorization", session)
                                    it
                                })
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(this.adminAuthenticationRequestJson)
                )
                .andExpect(status().isOk)
                .andExpect {
                    val rootNode = ObjectMapper().readTree(it.response.contentAsString)
                    assertEquals(session, rootNode.get("sessionKey").textValue())
                }

        //Test Authenticate with Session NO Body
        mockMvc
                .perform(
                        post("/auth/auth")
                                .with(RequestPostProcessor {
                                    it.addHeader("Authorization", session)
                                    it
                                })
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                )
                .andExpect(status().isOk)
                .andExpect {
                    val rootNode = ObjectMapper().readTree(it.response.contentAsString)
                    assertEquals(session, rootNode.get("sessionKey").textValue())
                }

        // New Session
        mockMvc
                .perform(
                        post("/auth/auth")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(this.adminAuthenticationRequestJson)
                )
                .andExpect(status().isOk)
                .andExpect {
                    val rootNode = ObjectMapper().readTree(it.response.contentAsString)
                    assertNotEquals(session, rootNode.get("sessionKey").textValue())
                }

    }
}