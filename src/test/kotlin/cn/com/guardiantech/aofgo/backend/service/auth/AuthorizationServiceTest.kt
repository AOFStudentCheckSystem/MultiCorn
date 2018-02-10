package cn.com.guardiantech.aofgo.backend.service.auth

import cn.com.guardiantech.aofgo.backend.BackendApplication
import cn.com.guardiantech.aofgo.backend.BackendApplicationTestConfiguration
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Role
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Subject
import cn.com.guardiantech.aofgo.backend.repository.auth.RoleRepository
import cn.com.guardiantech.aofgo.backend.repository.auth.SubjectRepository
import cn.com.guardiantech.aofgo.backend.request.authentication.admin.SubjectEditRequest
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by dedztbh on 18-2-10.
 * Project AOFGoBackend
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [BackendApplication::class])
@DataJpaTest
@Import(BackendApplicationTestConfiguration::class)
class AuthorizationServiceTest {

    @Autowired
    lateinit var authorizationService: AuthorizationService

    @Autowired
    private lateinit var subjectRepository: SubjectRepository

    @Autowired
    private lateinit var roleRepository: RoleRepository

    @Test
    fun editSubjectSetRole() {
        val newSubjectId = subjectRepository.save(Subject()).id
        val newRoleId = roleRepository.save(Role(roleName = "nnp")).id
        authorizationService.editSubjectSetRole(
                SubjectEditRequest(
                        id = newSubjectId,
                        subjectAttachedInfo = "nil",
                        roles = setOf("nnp")
                )
        )
        val s = subjectRepository.findById(newSubjectId).get()
        assertEquals("nil", s.subjectAttachedInfo)
        assertEquals(1, s.roles.size)
        assertEquals("nnp", s.roles.first().roleName)
    }
}