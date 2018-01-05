package cn.com.guardiantech.aofgo.backend.data.entity

import java.util.*
import javax.persistence.*

@Entity
class PermissionGrant(
        @Id
        @GeneratedValue
        val id: Long = -1,

        @ManyToOne
        @JoinColumn(name = "account")
        val account: Account,

        @Column(name = "ip")
        val ip: String,

        @Column(name = "time")
        val time: Date,

        @Column(name = "result")
        val result: Boolean,

        @Column(name = "note")
        val note: String
)