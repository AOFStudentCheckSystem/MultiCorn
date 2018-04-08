package cn.com.guardiantech.aofgo.backend.data.entity.veracross

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id


@Entity
class VeracrossCourseAttendence (
        @Id
        @GeneratedValue
        @Column(name = "attendence_id")
        var id: Long = -1,
)