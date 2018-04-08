package cn.com.guardiantech.aofgo.backend.data.entity.veracross

import cn.com.guardiantech.aofgo.backend.data.entity.Student
import javax.persistence.*


@Entity
@Table(uniqueConstraints = [(UniqueConstraint(columnNames = ["vCourse", "student"]))])
class VeracrossCourseAttendee (
        @Id
        @GeneratedValue
        @Column(name = "attendence_id")
        var id: Long = -1, // My Id changes every time... I guess //TODO

        @ManyToOne
        @JoinColumn(name = "vCourse")
        var courseAttending: VeracrossCourse? = null, //TODO I might be attending the `null` class though

        @ManyToOne
        @JoinColumn(name = "student")
        var student: Student,

        var grade: Float,

        @OneToMany
        var assignmentRecords: MutableSet<VeracrossAssignmentRecord> = hashSetOf()
)