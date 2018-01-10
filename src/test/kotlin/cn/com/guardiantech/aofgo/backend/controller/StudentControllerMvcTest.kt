package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.BackendApplication
import cn.com.guardiantech.aofgo.backend.BackendApplicationTestConfiguration
import cn.com.guardiantech.aofgo.backend.data.entity.Account
import cn.com.guardiantech.aofgo.backend.data.entity.AccountType
import cn.com.guardiantech.aofgo.backend.repository.AccountRepository
import cn.com.guardiantech.aofgo.backend.repository.StudentRepository
import cn.com.guardiantech.aofgo.backend.test.authutil.AuthenticationUtil
import org.json.JSONObject
import org.junit.Assert
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.request.RequestPostProcessor
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/**
 * Created by dedztbh on 1/10/18.
 * Project AOFGoBackend
 */

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [BackendApplication::class])
@DataJpaTest
@Import(BackendApplicationTestConfiguration::class)
@AutoConfigureMockMvc
class StudentControllerMvcTest {
    @Autowired private lateinit var mockMvc: MockMvc
    @Autowired private lateinit var studentRepo: StudentRepository
    @Autowired private lateinit var accountRepo: AccountRepository
    @Autowired private lateinit var authenticationUtil: AuthenticationUtil

    @Before
    fun initialize() {
        Assert.assertNotNull(mockMvc)
        authenticationUtil.prepare()
    }

    fun initAccount(): Account = accountRepo.save(
            Account(
                    firstName = "fn",
                    lastName = "ln",
                    email = "a@b.c",
                    phone = null,
                    type = AccountType.STUDENT,
                    preferredName = "pn"
            )
    )

    @Test
    fun createStudentTest() {
        val account = initAccount()
        mockMvc.perform(put("/student/create/")
                .with(RequestPostProcessor {
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                })
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                """{
                        "idNumber": "123456A",
                        "cardSecret": null,
                        "grade": 10,
                        "gender": "FEMALE",
                        "dateOfBirth": 612939600,
                        "dorm": "ELE",
                        "dormInfo": "DNE",
                        "accountId": ${account.id}
                    }""".trimIndent()
                ))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect {
                    assertEquals(1L, studentRepo.count())
                    assertEquals(studentRepo.findAll().first().idNumber,
                            JSONObject(it.response.contentAsString).getString("idNumber"))
                }
    }
}