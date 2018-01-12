package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.BackendApplication
import cn.com.guardiantech.aofgo.backend.BackendApplicationTestConfiguration
import cn.com.guardiantech.aofgo.backend.data.entity.Account
import cn.com.guardiantech.aofgo.backend.data.entity.AccountType
import cn.com.guardiantech.aofgo.backend.data.entity.Student
import cn.com.guardiantech.aofgo.backend.repository.AccountRepository
import cn.com.guardiantech.aofgo.backend.repository.StudentRepository
import cn.com.guardiantech.aofgo.backend.test.authutil.AuthenticationUtil
import org.json.JSONArray
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

    fun initStudent(): Student = studentRepo.save(
            Student(
                idNumber = "12345AB"
            )
    )

    @Test
    fun createStudentTest() {
        val account = initAccount()
        mockMvc.perform(put("/student/")
                .with({
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

        mockMvc.perform(put("/student/")
                .with({
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
                        "accountId": -1
                    }""".trimIndent()
                ))
                .andExpect(MockMvcResultMatchers.status().isNotFound)

        val account2 = initAccount()
        mockMvc.perform(put("/student/")
                .with({
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
                        "accountId": ${account2.id}
                    }""".trimIndent()
                ))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError)
    }

    @Test
    fun listAllStudentTest() {
        val stu = initStudent()
        mockMvc.perform(get("/student/listall")
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                }))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo {
                    assertEquals(stu.idNumber,
                            JSONArray(it.response.contentAsString)
                                    .getJSONObject(0)
                                    .getString("idNumber"))
                }
    }

    @Test
    fun getStudentByIdNumberTest() {
        val stu = initStudent()
        mockMvc.perform(get("/student/${stu.idNumber}")
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                }))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo {
                    assertEquals(stu.idNumber,
                            JSONObject(it.response.contentAsString)
                                    .getString("idNumber"))
                }
    }

    @Test
    fun createStudentWithAccountTest() {
        mockMvc.perform(put("/student/account")
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                })
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                        """{
                            "student": {
                                "idNumber": "123456A",
                                "cardSecret": null,
                                "grade": 10,
                                "gender": "FEMALE",
                                "dateOfBirth": 612939600,
                                "dorm": "ELE",
                                "dormInfo": "DNE"
                            },
                            "account": {
                            "firstName": "fn",
                            "lastName": "ln",
                            "email": "a@b.c",
                            "type": "OTHER",
                            "preferredName": "pn"
                            }
                        }""".trimIndent()
                ))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect {
                    assertEquals(1L, accountRepo.count())
                    assertEquals(1L, studentRepo.count())
                    assertEquals(studentRepo.findAll().first().idNumber,
                            JSONObject(it.response.contentAsString).getString("idNumber"))
                }
    }

    @Test
    fun editStudentTest() {
        var student = studentRepo.save(
                Student(
                        idNumber = "まほう",
                        cardSecret = "very fast",
                        account = initAccount()
                )
        )
        var origIdNum = student.idNumber
        mockMvc.perform(post("/student/")
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                })
                .content(
                        """
                            {
                                "idNumber": "まほう",
                                "cardSecret": "very slow",
                                "dorm": "T"
                            }
                        """.trimIndent()
                )
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk)
        student = studentRepo.findByIdNumber(student.idNumber).get()
        assertEquals(origIdNum, student.idNumber)
        origIdNum = student.idNumber
        assertEquals("very slow", student.cardSecret)
        assertEquals("T", student.dorm)

        val newAccount = initAccount()
        mockMvc.perform(post("/student/")
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                })
                .content(
                        """
                            {
                                "idNumber": "まほう",
                                "cardSecret": "",
                                "dorm": null,
                                "accountId": ${newAccount.id}
                            }
                        """.trimIndent()
                )
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk)
        student = studentRepo.findByIdNumber(student.idNumber).get()
        assertEquals(origIdNum, student.idNumber)
        assertEquals("", student.cardSecret)
        assertEquals("T", student.dorm)

        mockMvc.perform(post("/student/")
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                })
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)

        mockMvc.perform(post("/student/")
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                })
                .content("{\"idNumber\": \"DNE\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isNotFound)

        mockMvc.perform(post("/student/")
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                })
                .content(
                        """
                            {
                                "idNumber": "まほう",
                                "accountId": -1
                            }
                        """.trimIndent()
                )
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}