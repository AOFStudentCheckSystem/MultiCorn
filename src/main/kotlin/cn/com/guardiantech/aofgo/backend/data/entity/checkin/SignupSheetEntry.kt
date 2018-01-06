package cn.com.guardiantech.aofgo.backend.data.entity.checkin

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

/**
 * Created by Codetector on 2017/4/17.
 * Edited by DE_DZ_TBH on 2018/1/5.
 * Project backend
 */
@Entity
@Table(uniqueConstraints = [(UniqueConstraint(columnNames = arrayOf("event_group", "signup_sheet")))])
class SignupSheetEntry(
        @Id
        @GeneratedValue
        var id: Long = 0,

        @ManyToOne
        @OnDelete(action = OnDeleteAction.CASCADE)
        @JoinColumn(name = "event_group")
        var eventGroup: EventGroup,

        @ManyToOne
        @JoinColumn(name = "signup_sheet")
        var sheet: SignUpSheet,

        var weight: Long = 0 //Heavy objects float on top!!!
)