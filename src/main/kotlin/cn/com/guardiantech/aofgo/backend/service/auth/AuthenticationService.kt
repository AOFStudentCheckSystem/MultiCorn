package cn.com.guardiantech.aofgo.backend.service.auth

import cn.com.guardiantech.aofgo.backend.authentication.AuthenticationMechanism
import cn.com.guardiantech.aofgo.backend.data.entity.Account
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Credential
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Principal
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Session
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Subject
import cn.com.guardiantech.aofgo.backend.exception.UnauthorizedException
import cn.com.guardiantech.aofgo.backend.repository.auth.*
import cn.com.guardiantech.aofgo.backend.request.authentication.AuthenticationRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.RegisterRequest
import cn.com.guardiantech.aofgo.backend.service.AccountService
import cn.com.guardiantech.aofgo.backend.util.SessionUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

@Service
class AuthenticationService @Autowired constructor(
        private val subjectRepo: SubjectRepository,
        private val principalRepo: PrincipalRepository,
        private val credentialRepo: CredentialRepository,
        private val sessionRepo: SessionRepository,
        private val authenticationMechanism: AuthenticationMechanism,
        private val accountRepository: AccountRepository
) {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Value("\${auth.sessionTimeout}")
    private var sessionTimeout: Int = 60

    /**
     * @throws DataIntegrityViolationException Duplicate principal
     */
    @Transactional
    @Modifying
    fun register(registerRequest: RegisterRequest): Account {
        val newSubject = subjectRepo.save(Subject(
                subjectAttachedInfo = registerRequest.subjectAttachedInfo))

        val processedSecret = authenticationMechanism.encryptCredentialSecret(registerRequest.credential.type, registerRequest.credential.secret)

        principalRepo.save(Principal(
                type = registerRequest.principal.type,
                identification = registerRequest.principal.identification,
                owner = newSubject
        ))
        credentialRepo.save(Credential(
                type = registerRequest.credential.type,
                secret = processedSecret,
                owner = newSubject
        ))
        val account = accountRepository.save(Account(
                subject = newSubject)
        )
        entityManager.refresh(newSubject)
        return account
    }

    fun authenticate(authRequest: AuthenticationRequest): Session {
        //NoSuchElementException
        val principal = principalRepo.findByTypeAndIdentification(
                authRequest.principal.type,
                authRequest.principal.identification).get()
        val owner = principal.owner
        val ownerFoundCredential = owner.credentials.find {
            it.type == authRequest.credential.type && authenticationMechanism.verifyCredentialSecret(it, authRequest.credential.secret)
        }
        if (ownerFoundCredential !== null) {
            return sessionRepo.save(Session(
                    sessionKey = SessionUtil.secureRandomBase64Identifier(),
                    subject = owner,
                    authenticatedFactors = hashSetOf(ownerFoundCredential.type)
            ))
        } else {
            // Invalid Credential
            throw UnauthorizedException("Invalid username or password")
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

    fun verifySessionPermission(sessionKey: String, permission: Array<String>): Boolean {
        val sessionNullable = this.authenticateSession(sessionKey)
        return sessionNullable?.let { session ->
            val perms = session.getPermissions()
            return@let permission.all {
                perms.contains(it)
            }
        } ?: false
    }
}