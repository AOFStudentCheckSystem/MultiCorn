package cn.com.guardiantech.aofgo.backend.data.entity.remindernote

import javax.persistence.*

/**
 * Created by Codetector on 2018/04/06.
 * Project AOFGoBackend
 */
@Entity
class NoteTimer(
        @Enumerated(EnumType.STRING)
        val triggerType: NoteTimerTriggerType,

        val triggerValue: String,

        @ManyToOne
        @JoinColumn
        val note: ReminderNote,

        @GeneratedValue
        @Id
        val id: Long = -1
)