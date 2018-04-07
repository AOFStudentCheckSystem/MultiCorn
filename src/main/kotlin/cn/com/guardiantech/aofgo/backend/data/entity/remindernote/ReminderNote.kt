package cn.com.guardiantech.aofgo.backend.data.entity.remindernote

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * Created by Codetector on 2018/04/06.
 * Project AOFGoBackend
 */
@Entity
class ReminderNote (

        val noteTitle: String,
        val noteContent: String,

        var grants: Array<NoteAccessGrant> = arrayOf(),

        @Id
        @GeneratedValue
        val id: Long = -1
)