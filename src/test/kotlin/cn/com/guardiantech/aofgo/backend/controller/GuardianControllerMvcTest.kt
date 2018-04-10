package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.BackendApplication
import cn.com.guardiantech.aofgo.backend.BackendApplicationTestConfiguration
import cn.com.guardiantech.aofgo.backend.data.entity.*
import cn.com.guardiantech.aofgo.backend.repository.GuardianRepository
import cn.com.guardiantech.aofgo.backend.repository.StudentRepository
import cn.com.guardiantech.aofgo.backend.repository.auth.AccountRepository
import cn.com.guardiantech.aofgo.backend.test.authutil.AuthenticationUtil
import org.junit.Assert
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
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
class GuardianControllerMvcTest {
    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var studentRepo: StudentRepository
    @Autowired
    private lateinit var accountRepo: AccountRepository
    @Autowired
    private lateinit var authenticationUtil: AuthenticationUtil
    @Autowired
    private lateinit var guardianRepository: GuardianRepository

    @Before
    fun initialize() {
        Assert.assertNotNull(mockMvc)
        authenticationUtil.prepare()
    }

    fun initAccount(): Account = accountRepo.save(
            Account(
                    firstName = "fn",
                    lastName = "ln",
                    email = null,
                    phone = null,
                    type = AccountType.STUDENT,
                    preferredName = "pn"
            )
    )

    @Test
    fun setGuardianTest() {
        val a = accountRepo.save(
                Account(
                        firstName = "a",
                        lastName = "b",
                        email = null,
                        phone = null,
                        type = AccountType.FACULTY,
                        preferredName = "c"
                )
        )
        val b = accountRepo.save(
                Account(
                        firstName = "a1",
                        lastName = "b",
                        email = null,
                        phone = null,
                        type = AccountType.FACULTY,
                        preferredName = "c"
                )
        )
        val c = accountRepo.save(
                Account(
                        firstName = "a2",
                        lastName = "b",
                        email = null,
                        phone = null,
                        type = AccountType.FACULTY,
                        preferredName = "c"
                )
        )
        assertEquals(0L, studentRepo.count())
        val s = studentRepo.save(
                Student(
                        idNumber = "ejiffadfw",
                        account = initAccount(),
                        guardians = guardianRepository.save(
                                setOf(
                                        Guardian(
                                                guardianAccount = a,
                                                relation = GuardianType.COACH
                                        ),
                                        Guardian(
                                                guardianAccount = b,
                                                relation = GuardianType.PARENT
                                        )
                                )
                        ).toMutableSet()
                )
        )
        assertEquals(5L, accountRepo.count())
        assertEquals(2, s.guardians.size)
        println("account ids: a:${a.id} b:${b.id} c:${c.id}")

        mockMvc.perform(post("/student/${s.idNumber}/guardian/set")
                .with({
                    it.addHeader("Authorization", authenticationUtil.getSession().sessionKey)
                    it
                })
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                        """
                        [
                            {
                                "accountId": ${b.id},
                                "relation": "COACH"
                            },
                            {
                                "accountId": ${c.id},
                                "relation": "PARENT"
                            }
                        ]
                        """.trimIndent()
                ))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect {
                    assertEquals(5L, accountRepo.count())
                    assertEquals(1L, studentRepo.count())
                    assertEquals(2, studentRepo.findAll().first().guardians.size)
                    studentRepo.findAll().first().guardians.forEach {
                        println("account id found: ${it.guardianAccount.id}")
                        when {
                            it.guardianAccount.id == b.id -> {
                                assertEquals(GuardianType.COACH, it.relation)
                            }
                            it.guardianAccount.id == c.id -> {

                            }
                            else -> {

                            }
                        }
                    }
                }
    }

}