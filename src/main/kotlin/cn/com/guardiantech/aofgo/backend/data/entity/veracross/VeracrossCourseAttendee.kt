package cn.com.guardiantech.aofgo.backend.data.entity.veracross

import cn.com.guardiantech.aofgo.backend.data.entity.Student
import javax.persistence.*


@Entity
class VeracrossCourseAttendee (
        @Id
        @GeneratedValue
        @Column(name = "attendence_id")
        var id: Long = -1, // My Id changes every time... I guess //TODO

        @ManyToOne
        @JoinColumn
        var courseAttending: VeracrossCourse? = null, //TODO I might be attending the `null` class though

        @ManyToOne
        @JoinColumn
        var student: Student,

        var grade: Float,

        @OneToMany
        var assignmentRecords: MutableSet<VeracrossAssignmentRecord> = hashSetOf()
)