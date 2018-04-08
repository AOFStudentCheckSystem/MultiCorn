package cn.com.guardiantech.aofgo.backend.data.entity.veracross

import javax.persistence.*

/**
 * Created by Codetector on 2018/04/08.
 * Project AOFGoBackend
 */
@Entity
//TODO unique (assignment and course)
class VeracrossAssignmentRecord (
        @Id
        @GeneratedValue
        val id: Long = -1,

        @ManyToOne
        @JoinColumn
        var assignment: VeracrossAssignment,

        @ManyToOne
        @JoinColumn
        var courseAttendee: VeracrossCourseAttendee,

        var pointsEarned: Float = -1f
)