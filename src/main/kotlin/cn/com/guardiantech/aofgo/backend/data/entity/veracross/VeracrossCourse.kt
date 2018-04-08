package cn.com.guardiantech.aofgo.backend.data.entity.veracross

import javax.persistence.*

@Entity
class VeracrossCourse (
        @Id
        @GeneratedValue
        @Column(name = "class_id")
        var id: Long = -1,

        @Column(name = "class_identifier")
        var identifier: String? = null,

        @Column(name = "class_key")
        var key: String? = null,

        @OneToMany
        var assignment: MutableSet<VeracrossAssignment> = hashSetOf(),

        @OneToMany
        var attendees: MutableSet<VeracrossCourseAttendee> = hashSetOf()

)