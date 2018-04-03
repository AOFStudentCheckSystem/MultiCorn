package cn.com.guardiantech.aofgo.backend.service.note

import cn.com.guardiantech.aofgo.backend.data.entity.Notes.StickyNote
import cn.com.guardiantech.aofgo.backend.exception.EntityNotFoundException
import cn.com.guardiantech.aofgo.backend.repository.note.StickyNoteRepository
import cn.com.guardiantech.aofgo.backend.request.note.StickyNoteAddRequest
import cn.com.guardiantech.aofgo.backend.request.note.StickyNoteEditRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class StickyNoteService @Autowired constructor(
        val stickyNoteRepository: StickyNoteRepository
) {
    fun listAllNotes(): List<StickyNote> {
        return stickyNoteRepository.findAll().toList()
    }

    fun findById(id: Long): StickyNote {
        return stickyNoteRepository.findById(id).get()
    }

    @Transactional
    fun addNote(addRequest: StickyNoteAddRequest): StickyNote {
        val n = stickyNoteRepository.save(StickyNote(
                title = addRequest.title,
                text = addRequest.text
        ))
        return n
    }

    @Transactional
    fun deleteNote(id: Long): List<StickyNote> {
        return stickyNoteRepository.deleteById(id)
    }

    @Transactional
    fun editNote(editRequest: StickyNoteEditRequest): StickyNote {

        val theNote = try {
            stickyNoteRepository.findById(editRequest.id).get()
        } catch (e: NoSuchElementException) {
            throw EntityNotFoundException("Note not found")
        }

        if (editRequest.title != null) {
            theNote.title = editRequest.title
        }
        if (editRequest.text != null) {
            theNote.text = editRequest.text
        }

        return stickyNoteRepository.save(theNote)
    }
}