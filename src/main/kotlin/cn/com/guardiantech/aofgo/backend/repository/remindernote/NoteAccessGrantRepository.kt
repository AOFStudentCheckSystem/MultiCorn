package cn.com.guardiantech.aofgo.backend.repository.remindernote

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Subject
import cn.com.guardiantech.aofgo.backend.data.entity.remindernote.NoteAccessGrant
import cn.com.guardiantech.aofgo.backend.data.entity.remindernote.ReminderNote
import org.springframework.data.repository.CrudRepository
import java.util.*

/**
 * Created by Codetector on 2018/04/08.
 * Project AOFGoBackend
 */
interface NoteAccessGrantRepository: CrudRepository<NoteAccessGrant, Long> {
    fun findByNoteAndUser(note: ReminderNote, user: Subject): Optional<NoteAccessGrant>
}