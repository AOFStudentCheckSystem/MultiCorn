package cn.com.guardiantech.aofgo.backend.data.entity.veracross

import javax.persistence.*

/**
 * Created by Codetector on 2018/04/08.
 * Project AOFGoBackend
 */
@Entity
@Table(uniqueConstraints = [(UniqueConstraint(columnNames = ["vAssignment", "vAttendee"]))])
class VeracrossAssignmentRecord (
        @Id
        @GeneratedValue
        val id: Long = -1,

        @ManyToOne
        @JoinColumn(name = "vAssignment")
        var assignment: VeracrossAssignment,

        @ManyToOne
        @JoinColumn(name = "vAttendee")
        var courseAttendee: VeracrossCourseAttendee

//        var pointsEarned: Float = -1f
//TODO: find Veracross API for this
)