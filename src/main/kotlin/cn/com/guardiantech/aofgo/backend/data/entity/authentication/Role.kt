package cn.com.guardiantech.aofgo.backend.data.entity.authentication

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [(UniqueConstraint(columnNames = ["role_name"]))])
class Role(
        @Id
        @GeneratedValue
        @Column(name = "role_id")
        val id: Long = -1,

        @Column(name = "role_name", unique = true)
        val roleName: String,

        @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST])
        @JoinTable(
                name = "roles_permissions",
                joinColumns = [(JoinColumn(name = "role_id", referencedColumnName = "role_id", nullable = false))],
                inverseJoinColumns = [(JoinColumn(name = "permission_id", referencedColumnName = "permission_id", nullable = false))])
        @OrderBy("permission_key ASC")
        val permissions: MutableList<Permission> = arrayListOf()
)