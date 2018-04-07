package cn.com.guardiantech.aofgo.backend.data.entity.remindernote

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * Created by Codetector on 2018/04/06.
 * Project AOFGoBackend
 */
@Entity
class NoteTimer(
        val triggerType: NoteTimerTriggerType,
        val triggerValue: String,

        val note: ReminderNote,
        @GeneratedValue
        @Id
        val id: Long = -1
)