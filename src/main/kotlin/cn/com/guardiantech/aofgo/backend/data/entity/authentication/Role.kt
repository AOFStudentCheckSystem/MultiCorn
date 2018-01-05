package cn.com.guardiantech.aofgo.backend.data.entity.authentication

import javax.persistence.*

@Entity
class Role(
        @Id
        @GeneratedValue
        @Column(name = "role_id")
        val roleId: Long = -1,

        @Column(name = "role_name", unique = true)
        val roleName: String,

        @ManyToMany
        @JoinTable(
                name = "roles_permissions",
                joinColumns = [(JoinColumn(name = "role_id", referencedColumnName = "role_id", nullable = false))],
                inverseJoinColumns = [(JoinColumn(name = "permission_id", referencedColumnName = "permission_id", nullable = false))])
        val permissions: MutableSet<Permission> = hashSetOf()
)