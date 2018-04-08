package cn.com.guardiantech.aofgo.backend.service.remindernote

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Subject
import cn.com.guardiantech.aofgo.backend.data.entity.remindernote.NoteAccessGrant
import cn.com.guardiantech.aofgo.backend.data.entity.remindernote.NoteAccessGrantType
import cn.com.guardiantech.aofgo.backend.data.entity.remindernote.ReminderNote
import cn.com.guardiantech.aofgo.backend.exception.EntityNotFoundException
import cn.com.guardiantech.aofgo.backend.repository.remindernote.NoteAccessGrantRepository
import cn.com.guardiantech.aofgo.backend.repository.remindernote.ReminderNoteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by Codetector on 2018/04/08.
 * Project AOFGoBackend
 */
@Service
class ReminderNoteService @Autowired constructor(
        private val reminderNoteRepository: ReminderNoteRepository,
        private val noteAccessGrantRepository: NoteAccessGrantRepository
) {
    @Transactional
    fun createNote(title: String, content: String, owner: Subject): ReminderNote {
        val note = reminderNoteRepository.save(ReminderNote(title, content))
        val grant = noteAccessGrantRepository.save(NoteAccessGrant(
                note,
                owner,
                NoteAccessGrantType.OWNER
        ))
        return note
    }

    @Transactional
    fun grantPermission(note: ReminderNote, user: Subject, type: NoteAccessGrantType) {
        val grant = noteAccessGrantRepository.findByNoteAndUser(note, user).orElseGet { NoteAccessGrant(note, user, type) }
        grant.grantType = type
        noteAccessGrantRepository.save(grant)
    }

    /**
     * @return - Boolean, false means failed to remove, (There needs to be at least one owner)
     */
    @Transactional
    fun revokeAccess(note: ReminderNote, user: Subject): Boolean {
        val grant = noteAccessGrantRepository.findByNoteAndUser(note, user).orElseThrow { EntityNotFoundException("Failed to locate the requested grant") }
        if ((grant.grantType != NoteAccessGrantType.OWNER) || (note.grants.any { ((it != grant) && (it.grantType == NoteAccessGrantType.OWNER)) }) ) {
            noteAccessGrantRepository.delete(grant)
            return true
        }
        return false
    }

    @Transactional
    fun editNote(note: ReminderNote, newTitle: String? = null, newContent: String? = null): ReminderNote {
        var changed = false
        newTitle?.let {
            changed = true
            note.noteTitle = newTitle
        }
        newContent?.let {
            changed = true
            note.noteContent = newContent
        }
        return if (changed) {
            reminderNoteRepository.save(note)
        } else {
            note
        }
    }
}