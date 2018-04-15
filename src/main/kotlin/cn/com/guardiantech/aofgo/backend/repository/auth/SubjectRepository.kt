package cn.com.guardiantech.aofgo.backend.repository.auth

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Principal
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.PrincipalType
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Subject
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface SubjectRepository : CrudRepository<Subject, Long> {
    fun findById(id: Long): Optional<Subject>
    @Query("select s from Subject s join s.principals sprincipals where (sprincipals.type = ?1 and sprincipals.identification = ?2)")
    fun findByInPrincipals(type: PrincipalType, value: String): Optional<Subject>
}