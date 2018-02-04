package cn.com.guardiantech.aofgo.backend.data.entity.authentication

import cn.com.guardiantech.aofgo.backend.jsonview.SubjectView
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonView
import javax.persistence.*

/**
 * Created by Codetector on 29/11/2017.
 * Project aofgo-backend
 */
@Entity
class Subject(
        @Id
        @GeneratedValue
        @Column(name = "subject_id", unique = true)
        @get:JsonView(SubjectView.BriefView::class)
        val id: Long = -1,


        @OneToMany(mappedBy = "owner", orphanRemoval = true, fetch = FetchType.EAGER)
        @JsonManagedReference
        @get:JsonView(SubjectView.AuthenticationView::class)
        val principals: MutableSet<Principal> = hashSetOf(),

        @OneToMany(mappedBy = "owner", orphanRemoval = true, fetch = FetchType.EAGER)
        @JsonManagedReference
        @get:JsonView(SubjectView.AdminView::class)
        val credentials: MutableSet<Credential> = hashSetOf(),

        @Lob
        @Column(nullable = true)
        @get:JsonView(SubjectView.AuthenticationView::class)
        val subjectAttachedInfo: String? = null,

        @ManyToMany(fetch = FetchType.EAGER)
        @get:JsonView(SubjectView.AdminView::class)
        @JoinTable(
                name = "subject_roles",
                joinColumns = [(JoinColumn(name = "subject_id", referencedColumnName = "subject_id", nullable = false))],
                inverseJoinColumns = [(JoinColumn(name = "role_id", referencedColumnName = "role_id", nullable = false))])
        val roles: MutableSet<Role> = hashSetOf()
) {
    fun addPrincipal(p: Principal) {
        p.owner = this
        principals.add(p)
    }

    fun addCredential(c: Credential) {
        c.owner = this
        credentials.add(c)
    }

    @JsonProperty("allPermissions")
    @JsonView(SubjectView.BriefView::class)
    fun allPermissions(): Set<Permission> {
        return roles.fold(hashSetOf(), { sum, r ->
            sum.addAll(r.permissions)
            sum
        })
    }
}