package cn.com.guardiantech.aofgo.backend.data.entity.veracross

import cn.com.guardiantech.aofgo.backend.data.entity.Student
import javax.persistence.*

@Entity
class VeracrossCookie (
        @Id
        @GeneratedValue
        @Column(name = "id")
        var id: Long = -1,

        @OneToOne
        @JoinColumn(name = "student", unique = true)
        var student: Student,

        @Column(name = "veracross_session", unique = true)
        var session: String? = null
)