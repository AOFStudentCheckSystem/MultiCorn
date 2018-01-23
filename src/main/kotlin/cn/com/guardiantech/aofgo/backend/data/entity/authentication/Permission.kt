package cn.com.guardiantech.aofgo.backend.data.entity.authentication

import javax.persistence.*

@Entity
@Table(uniqueConstraints = [(UniqueConstraint(columnNames = ["permission_key"]))])
class Permission(
        @Id
        @GeneratedValue
        @Column(name = "permission_id")
        val id: Long = -1,

        @Column(name = "permission_key", unique = true)
        val permissionKey: String
)