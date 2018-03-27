package cn.com.guardiantech.aofgo.backend.data.entity.slip

import cn.com.guardiantech.aofgo.backend.data.entity.Guardian
import java.util.*
import javax.persistence.*

@Entity
class PermissionRequest(
        @Id
        @GeneratedValue
        val id: Long = -1,

        @ManyToOne
        @JoinColumn
        val acceptor: Guardian,

        @Column(name = "ip")
        var acceptorIp: String = "",

        @Column(name = "time")
        var acceptTime: Date = Date(0),

        @Column(name = "result")
        var accepted: Boolean? = null,

        @Column(name = "note")
        var note: String = ""
) {
    @ManyToOne
    lateinit var campusLeaveRequest: CampusLeaveRequest
}