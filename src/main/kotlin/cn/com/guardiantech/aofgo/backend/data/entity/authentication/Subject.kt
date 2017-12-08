package cn.com.guardiantech.aofgo.backend.data.entity.authentication

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import javax.persistence.*

/**
 * Created by Codetector on 29/11/2017.
 * Project aofgo-backend
 */
@Entity
class Subject(
        @Id
        @GeneratedValue
        val id: Long = -1,

        @OneToMany(mappedBy = "owner", orphanRemoval = true)
        val principals: MutableSet<Principal> = hashSetOf(),

        @OneToMany(mappedBy = "owner", orphanRemoval = true)
        val credentials: MutableSet<Credential> = hashSetOf(),

        @Lob
        @Column(nullable = true)
        val subjectAttachedInfo: String? = null
) {
    fun addPrincipal(p: Principal) {
        p.owner = this
        principals.add(p)
    }

    fun addCredential(c: Credential) {
        c.owner = this
        credentials.add(c)
    }
}