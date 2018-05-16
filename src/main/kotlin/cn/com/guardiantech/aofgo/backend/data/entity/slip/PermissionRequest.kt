package cn.com.guardiantech.aofgo.backend.data.entity.slip

import cn.com.guardiantech.aofgo.backend.data.entity.Guardian
import cn.com.guardiantech.aofgo.backend.jsonview.SlipView
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonView
import java.util.*
import javax.persistence.*

@Entity
class PermissionRequest(
        @Id
        @GeneratedValue
        @JsonView(SlipView.FacultyView::class)
        val id: Long = -1,

        @ManyToOne
        @JoinColumn
        @JsonView(SlipView.FullView::class)
        val acceptor: Guardian,

        @Column(name = "ip")
        @JsonView(SlipView.FullView::class)
        var acceptorIp: String = "",

        @Column(name = "time")
        var acceptTime: Date = Date(0),

        @Column(name = "result")
        @JsonView(SlipView.FacultyView::class)
        var accepted: Boolean? = null,

        @Column(name = "note")
        @JsonView(SlipView.FacultyView::class)
        var note: String = ""
) {
    @ManyToOne
    @JsonView(SlipView.FacultyView::class)
    @JsonManagedReference
    lateinit var campusLeaveRequest: CampusLeaveRequest
}