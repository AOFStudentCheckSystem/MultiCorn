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
import org.springframework.stereotype.Service

@Service
class AuthenticationService @Autowired constructor(
        val subjectRepo: SubjectRepository,
        val principalRepo: PrincipalRepository,
        val credentialRepo: CredentialRepository,
        val sessionRepo: SessionRepository
) {

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
}