package cn.com.guardiantech.aofgo.backend.test.authutil

import cn.com.guardiantech.aofgo.backend.BackendApplication
import cn.com.guardiantech.aofgo.backend.BackendApplicationTestConfiguration
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Assert.assertEquals
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.RequestPostProcessor
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [BackendApplication::class])
@DataJpaTest
@Import(BackendApplicationTestConfiguration::class)
@AutoConfigureMockMvc

class AuthUtilTest {

    @Autowired
    private lateinit var authenticationUtil: AuthenticationUtil

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Before
    fun prepare() {
        authenticationUtil.prepare()
    }

    @Test
    fun testAuthenticationUtilSessionMagic() {
        val session = authenticationUtil.getSession()

        mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/auth/auth")
                                .with(RequestPostProcessor {
                                    it.addHeader("Authorization", session.sessionKey)
                                    it
                                })
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect {
                    val rootNode = ObjectMapper().readTree(it.response.contentAsString)
                    assertEquals(session.sessionKey, rootNode.get("sessionKey").textValue())
                }
    }

}