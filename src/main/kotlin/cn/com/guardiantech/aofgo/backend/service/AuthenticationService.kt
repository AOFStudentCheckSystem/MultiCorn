package cn.com.guardiantech.aofgo.backend.service

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Credential
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Principal
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Session
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Subject
import cn.com.guardiantech.aofgo.backend.repository.CredentialRepository
import cn.com.guardiantech.aofgo.backend.repository.PrincipalRepository
import cn.com.guardiantech.aofgo.backend.repository.SessionRepository
import cn.com.guardiantech.aofgo.backend.repository.SubjectRepository
import cn.com.guardiantech.aofgo.backend.request.authentication.AuthenticationRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.RegisterRequest
import cn.com.guardiantech.aofgo.backend.util.SessionUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticationService @Autowired constructor(
        private val subjectRepo: SubjectRepository,
        private val principalRepo: PrincipalRepository,
        private val credentialRepo: CredentialRepository,
        private val sessionRepo: SessionRepository
) {

    @Value("\${auth.sessionTimeout}")
    private var sessionTimeout: Int = 60

    fun register(registerRequest: RegisterRequest) {
        val newSubject = subjectRepo.save(Subject(
                subjectAttachedInfo = registerRequest.subjectAttachedInfo))
        principalRepo.save(Principal(
                type = registerRequest.principal.type,
                identification = registerRequest.principal.identification,
                owner = newSubject
        ))
        credentialRepo.save(Credential(
                type = registerRequest.credential.type,
                secret = registerRequest.credential.secret,
                owner = newSubject
        ))
    }

    fun authenticate(authRequest: AuthenticationRequest): Session {
        //NoSuchElementException
        val principle = principalRepo.findByTypeAndIdentification(
                authRequest.principal.type,
                authRequest.principal.identification).get()
        val owner = principle.owner
        val ownerFoundCredential = owner.credentials.find {
            it.type == authRequest.credential.type && it.secret == authRequest.credential.secret
        }
        if (ownerFoundCredential !== null) {
            return sessionRepo.save(Session(
                    sessionKey = SessionUtil.secureRandomBase64Identifier(),
                    subject = owner,
                    authenticatedFactors = mutableSetOf(ownerFoundCredential.type)
            ))
        } else {
            // Invalid Credential
            throw RuntimeException()
        }
    }

    fun authenticateSession(session: String): Session? {
        val sessionFind = sessionRepo.findBySessionKey(session)

        val expireDate = Calendar.getInstance()
        expireDate.time = Date()
        expireDate.add(Calendar.SECOND, sessionTimeout)

        if (sessionFind.isPresent) {
            val returnSession = sessionFind.get()
            if (returnSession.accessTimestamp.before(expireDate.time)) {
                returnSession.accessTimestamp = Date()
                sessionRepo.save(returnSession)
                return returnSession
            }
        }
        return null
    }
}