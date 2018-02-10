package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.BackendApplication
import cn.com.guardiantech.aofgo.backend.BackendApplicationTestConfiguration
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Role
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Subject
import cn.com.guardiantech.aofgo.backend.repository.auth.RoleRepository
import cn.com.guardiantech.aofgo.backend.repository.auth.SubjectRepository
import cn.com.guardiantech.aofgo.backend.test.authutil.AuthenticationUtil
import com.fasterxml.jackson.core.JsonFactory
import junit.framework.Assert.assertEquals
import org.junit.Assert
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/**
 * Created by dedztbh on 18-2-4.
 * Project AOFGoBackend
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [BackendApplication::class])
@DataJpaTest
@Import(BackendApplicationTestConfiguration::class)
@AutoConfigureMockMvc
class AuthenticationAdminControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var authUtil: AuthenticationUtil

    @Autowired
    private lateinit var subjectRepository: SubjectRepository

    @Autowired
    private lateinit var roleRepository: RoleRepository

    @Before
    fun initialize() {
        Assert.assertNotNull(mockMvc)
        authUtil.prepare()
    }

    @Test
    fun editSubjectSetRole() {
        val newSubjectId = subjectRepository.save(Subject()).id
        val newRoleId = roleRepository.save(Role(roleName = "nnp")).id

        mockMvc
                .perform(MockMvcRequestBuilders.post("/auth/admin/subject")
                        .with({
                            it.addHeader("Authorization", authUtil.getSession().sessionKey)
                            it
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "id":$newSubjectId,
                        "subjectAttachedInfo":"nil",
                        "roles":["nnp"]
                    }
                """.trimIndent()))
                .andExpect(MockMvcResultMatchers.status().isOk)
        val s = subjectRepository.findById(newSubjectId).get()
        assertEquals("nil", s.subjectAttachedInfo)
        assertEquals(1L, s.roles)
        assertEquals("nnp", s.roles.first().roleName)
    }
}