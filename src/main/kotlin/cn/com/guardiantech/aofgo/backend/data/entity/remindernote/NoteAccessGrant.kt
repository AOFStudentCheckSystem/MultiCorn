package cn.com.guardiantech.aofgo.backend.data.entity.remindernote

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Subject
import javax.persistence.*

/**
 * Created by Codetector on 2018/04/06.
 * Project AOFGoBackend
 */
@Entity
class NoteAccessGrant(
        @ManyToOne
        @JoinColumn
        val note: ReminderNote,
        @ManyToOne
        val user: Subject,
        @Enumerated(EnumType.STRING)
        var grantType: NoteAccessGrantType,
        @Id
        @GeneratedValue
        val grantId: Long = -1
)