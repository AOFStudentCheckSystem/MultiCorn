package cn.com.guardiantech.aofgo.backend.service

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Credential
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Principal
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Session
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Subject
import cn.com.guardiantech.aofgo.backend.request.authentication.AuthenticationRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.RegisterRequest
import cn.com.guardiantech.aofgo.backend.util.SessionUtil

class AuthenticationService {

    fun register(registerRequest: RegisterRequest) {
//        entityManager {
//            transactional(it) {
//                val newSubject = Subject(
//                        subjectAttachedInfo = registerRequest.subjectAttachedInfo)
//                val newPrincipal = Principal(
//                        type = registerRequest.principal.type,
//                        identification = registerRequest.principal.identification,
//                        owner = newSubject
//                )
//                val newCredential = Credential(
//                        type = registerRequest.credential.type,
//                        secret = registerRequest.credential.secret,
//                        owner = newSubject
//                )
//                it.persist(newSubject)
//                it.merge(newPrincipal)
//                it.merge(newCredential)
//                it.flush()
//            }
//        }
    }

    fun authenticate(authRequest: AuthenticationRequest): Session {
//        return entityManager {
//            transactional(it) {
//                val principle = it
//                        .createQuery("FROM Principal P WHERE (P.type = :tipe) AND (P.identification = :identification)")
//                        .setParameter("tipe", authRequest.principal.type)
//                        .setParameter("identification", authRequest.principal.identification)
//                        // NoResultException: Invalid Principal
//                        .singleResult as Principal
//                val owner = principle.owner
//                val ownerFoundCredential = owner.credentials.find {
//                    it.type == authRequest.credential.type && it.secret == authRequest.credential.secret
//                }
//                if (ownerFoundCredential !== null) {
//                    val newSession = it.merge(Session(
//                            sessionKey = SessionUtil.secureRandomBase64Identifier(),
//                            subject = owner,
//                            authenticatedFactors = mutableSetOf(ownerFoundCredential.type)
//                    ))
//                    it.flush()
//                    it.refresh(newSession)
//                    newSession
//                } else {
//                    // Invalid Credential
//                    throw RuntimeException()
//                }
//            }
//        }
        TODO()
    }
}