package cn.com.guardiantech.aofgo.backend.data.entity.authentication

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

/**
 * Created by Codetector on 01/12/2017.
 * Project aofgo-backend
 */
@Entity
class Session (
        @Id
        @GeneratedValue
        val id: Long = -1,

        val sessionKey: String,

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn
        @JsonBackReference
        val subject: Subject,

        @ElementCollection
        @CollectionTable(joinColumns = [(JoinColumn(name = "id"))])
        @Column(name = "authenticated_factor")
        val authenticatedFactors: MutableSet<CredentialType> = hashSetOf()
) {
    @JsonProperty("permission")
    fun getPermissions(): Set<String> {
        // TODO Implement Permission Grid
        return hashSetOf()
    }

    @JsonProperty("isAuthenticated")
    fun isAuthenticated(): Boolean = authenticatedFactors.isNotEmpty()

    @JsonProperty("isAuthorized")
    fun isAuthorized(): Boolean = isAuthenticated() // TODO Change when Implement MFA
}