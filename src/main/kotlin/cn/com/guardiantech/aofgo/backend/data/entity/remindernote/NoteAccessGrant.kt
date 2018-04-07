package cn.com.guardiantech.aofgo.backend.data.entity.remindernote

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Subject
import javax.persistence.Entity

/**
 * Created by Codetector on 2018/04/06.
 * Project AOFGoBackend
 */
@Entity
class NoteAccessGrant(
        val note: ReminderNote,
        val user: Subject,
        val grantType: NoteAccessGrantType
)