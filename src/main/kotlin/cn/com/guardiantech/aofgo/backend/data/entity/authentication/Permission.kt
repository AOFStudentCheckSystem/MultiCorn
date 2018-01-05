package cn.com.guardiantech.aofgo.backend.data.entity.authentication

import javax.persistence.*

@Entity
class Permission(
        @Id
        @GeneratedValue
        @Column(name = "permission_id")
        val permissionId: Long = -1,

        @Column(name = "permission_key", unique = true)
        val permissionKey: String
)