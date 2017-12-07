package cn.com.guardiantech.aofgo.backend.service

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Credential
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Principal
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Subject
import cn.com.guardiantech.aofgo.backend.request.authentication.RegisterRequest
import cn.com.guardiantech.aofgo.backend.util.entityManager
import cn.com.guardiantech.aofgo.backend.util.transactional

class AuthenticationSerivce {

    fun register(registerRequest: RegisterRequest) {
        entityManager {
            transactional(it) {
                val newSubject = Subject(
                        subjectAttachedInfo = registerRequest.subjectAttachedInfo)
                val newPrincipal = Principal(
                        type = registerRequest.principal.type,
                        identification = registerRequest.principal.identification,
                        owner = newSubject
                )
                val newCredential = Credential(
                        type = registerRequest.credential.type,
                        secret = registerRequest.credential.secret,
                        owner = newSubject
                )
                it.persist(newSubject)
                it.merge(newPrincipal)
                it.merge(newCredential)
                it.flush()
            }
        }
    }
}