package cn.com.guardiantech.aofgo.backend.data.entity.authentication

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.persistence.*

/**
 * Created by Codetector on 01/12/2017.
 * Project aofgo-backend
 */
@Entity
class Session(
        @Id
        @GeneratedValue
        val id: Long = -1,

        val sessionKey: String,

        @ManyToOne
        @JoinColumn
        @JsonBackReference
        val subject: Subject,

        @ElementCollection
        @CollectionTable(joinColumns = [(JoinColumn(name = "id"))])
        @Column(name = "authenticated_factor")
        val authenticatedFactors: MutableSet<CredentialType> = hashSetOf(),

        @Column(nullable = false)
        @Temporal(TemporalType.TIMESTAMP)
        val creationTimestamp: Date = Date(),

        @Column(nullable = false, updatable = true, name = "accessed")
        @Temporal(TemporalType.TIMESTAMP)
        var accessTimestamp: Date = Date()
) {
    @JsonProperty("permission")
    fun getPermissions(): Set<String> {
        return subject.allPermissions().map { it.permissionKey }.toSet()
    }

    @JsonProperty("isAuthenticated")
    fun isAuthenticated(): Boolean = authenticatedFactors.isNotEmpty()

    @JsonProperty("isAuthorized")
    fun isAuthorized(): Boolean = isAuthenticated() // TODO Change when Implement MFA
}