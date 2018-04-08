package cn.com.guardiantech.aofgo.backend.data.entity.veracross

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
        var dueDate: Date? = null,

        @Column(name = "veracross_identifier")
        var identifier: String? = null,

        @Column(name = "last_update")
        @Temporal(TemporalType.TIMESTAMP)
        var lastUpdate: Date? = null
)