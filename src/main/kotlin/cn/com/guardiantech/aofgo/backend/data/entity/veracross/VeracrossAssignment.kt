package cn.com.guardiantech.aofgo.backend.data.entity.veracross

import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*

@Entity
class VeracrossAssignment(
        @Id
        @GeneratedValue
        @Column(name = "assignment_id")
        var id: Long = -1,

        @Column(name = "due_date")
        @Temporal(TemporalType.TIMESTAMP)
        var dueDate: Date,

        @Column(name = "veracross_identifier")
        var identifier: String,

        @ManyToOne
        @JoinColumn
        var clazz: VeracrossCourse,

//        var possiblePoint: Float,
//TODO: find Veracross API for this

        @Column(name = "last_update")
        @Temporal(TemporalType.TIMESTAMP)
        @UpdateTimestamp
        var lastUpdate: Date? = null
)