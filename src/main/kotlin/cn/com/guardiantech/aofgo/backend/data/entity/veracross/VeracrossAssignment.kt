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
        var dueDate: Date? = null, //TODO Homework is never due cool!

        @Column(name = "veracross_identifier")
        var identifier: String? = null, //TODO I guess veracross forgot to generate an identifier for it.

        @ManyToOne
        @JoinColumn
        var clazz: VeracrossCourse? = null, //TODO I just learned from Mr. Calvin that nullable is very good.

        var possiblePoint: Float,

        @Column(name = "last_update")
        @Temporal(TemporalType.TIMESTAMP)
        @UpdateTimestamp
        var lastUpdate: Date? = null
)