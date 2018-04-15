package cn.com.guardiantech.aofgo.backend.data.entity.remindernote

import javax.persistence.*

/**
 * Created by Codetector on 2018/04/06.
 * Project AOFGoBackend
 */
@Entity
class ReminderNote (

        var noteTitle: String,
        var noteContent: String,

        @OneToMany
        var grants: Set<NoteAccessGrant> = hashSetOf(),

        @OneToMany
        var timers: Set<NoteTimer> = hashSetOf(),

        @Id
        @GeneratedValue
        val id: Long = -1
)