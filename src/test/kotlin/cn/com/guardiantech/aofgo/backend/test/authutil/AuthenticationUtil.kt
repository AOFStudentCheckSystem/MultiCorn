package cn.com.guardiantech.aofgo.backend.test.authutil

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.CredentialType
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.PrincipalType
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Session
import cn.com.guardiantech.aofgo.backend.repository.auth.CredentialRepository
import cn.com.guardiantech.aofgo.backend.repository.auth.PrincipalRepository
import cn.com.guardiantech.aofgo.backend.repository.auth.SubjectRepository
import cn.com.guardiantech.aofgo.backend.request.authentication.AuthenticationRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.CredentialRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.PrincipalRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.RegisterRequest
import cn.com.guardiantech.aofgo.backend.service.AuthenticationService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class AuthenticationUtil @Autowired constructor(
        private val authService: AuthenticationService,
        private val subjectRepository: SubjectRepository,
        private val credentialRepository: CredentialRepository,
        private val principalRepository: PrincipalRepository
) {
    private var userSession: Session? = null

    fun prepare() {
        assertEquals("Non Empty Repository (Subject)", 0, subjectRepository.count())
        assertEquals("Non Empty Repository (Credential)", 0, credentialRepository.count())
        assertEquals("Non Empty Repository (Principal)", 0, principalRepository.count())

        authService.register(
                RegisterRequest(
                        principal = PrincipalRequest(
                                type = PrincipalType.USERNAME,
                                identification = "magic"
                        ),
                        credential = CredentialRequest(
                                type = CredentialType.PASSWORD,
                                secret = "magic2"
                        ),
                        subjectAttachedInfo = ""
                )
        )

        this.userSession = authService.authenticate(AuthenticationRequest(
                principal = PrincipalRequest(
                        type = PrincipalType.USERNAME,
                        identification = "magic"
                ),
                credential = CredentialRequest(
                        type = CredentialType.PASSWORD,
                        secret = "magic2"
                )
        ))

        assertNotNull("found NULL for the session, check Authentication System.", this.userSession)
    }

    fun getSession(): Session {
        assertNotNull("Found NULL session, did you call prepare in your @Before method?", this.userSession)
        return this.userSession!! // I know this is kinda unsafe, but it should be find, since the userSession has a private scope.
    }
}