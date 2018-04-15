package cn.com.guardiantech.aofgo.backend.service.auth

import cn.com.guardiantech.aofgo.backend.BackendApplication
import cn.com.guardiantech.aofgo.backend.BackendApplicationTestConfiguration
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.*
import cn.com.guardiantech.aofgo.backend.repository.auth.CredentialRepository
import cn.com.guardiantech.aofgo.backend.repository.auth.PrincipalRepository
import cn.com.guardiantech.aofgo.backend.repository.auth.SubjectRepository
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by dedztbh on 18-4-15.
 * Project AOFGoBackend
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [BackendApplication::class])
@DataJpaTest
@Import(BackendApplicationTestConfiguration::class)
class AuthenticationServiceTest {

    @Autowired
    lateinit var authenticationService: AuthenticationService

    @Autowired
    private lateinit var subjectRepository: SubjectRepository

    @Autowired
    private lateinit var credentialRepository: CredentialRepository

    @Autowired
    private lateinit var principalRepository: PrincipalRepository

    @Test
    fun subjectExistsTest() {
        val sub = subjectRepository.save(
                Subject(
                        subjectAttachedInfo = "I am the best"
                )
        )

        Assert.assertEquals(false, authenticationService.subjectExists(
                PrincipalType.EMAIL,
                "a@b.com"
        ))

        val princ = principalRepository.save(
                Principal(
                        type = PrincipalType.EMAIL,
                        identification = "a@b.com",
                        owner = sub
                )
        )
        val cred = credentialRepository.save(
                Credential(
                        type = CredentialType.PASSWORD,
                        secret = "1234567890",
                        owner = sub
                )
        )

        Assert.assertEquals(true, authenticationService.subjectExists(
                PrincipalType.EMAIL,
                "a@b.com"
        ))
    }
}