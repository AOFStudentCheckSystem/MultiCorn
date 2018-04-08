package cn.com.guardiantech.aofgo.backend.data.entity.veracross

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class VeracrossCookie (
        @Id
        @GeneratedValue
        @Column(name = "student_id")
        var id: Long = -1,

        @Column(name = "veracross_session", unique = true)
        var session: String? = null
)