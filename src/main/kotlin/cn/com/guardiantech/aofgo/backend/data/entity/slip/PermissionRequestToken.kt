package cn.com.guardiantech.aofgo.backend.data.entity.slip

import java.util.*
import javax.persistence.*

/**
 * Created by dedztbh on 18-5-14.
 * Project AOFGoBackend
 */
@Entity
class PermissionRequestToken(
        @Id
        @GeneratedValue
        val id: Long = -1,

        @Column(name = "token", unique = true)
        var token: String = "",

        @Column(name = "created_time")
        var createdTime: Date = Date(),

        @OneToOne
        @JoinColumn(name = "permission_request")
        var permissionRequest: PermissionRequest
)