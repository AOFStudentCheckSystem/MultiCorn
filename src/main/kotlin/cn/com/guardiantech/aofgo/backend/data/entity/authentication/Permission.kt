package cn.com.guardiantech.aofgo.backend.data.entity.authentication

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

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