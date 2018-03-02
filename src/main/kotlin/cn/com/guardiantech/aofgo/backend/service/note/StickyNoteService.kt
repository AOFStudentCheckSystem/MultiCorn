package cn.com.guardiantech.aofgo.backend.service.note

import cn.com.guardiantech.aofgo.backend.data.entity.Notes.StickyNote
import cn.com.guardiantech.aofgo.backend.exception.EntityNotFoundException
import cn.com.guardiantech.aofgo.backend.repository.note.StickyNotesRepository
import cn.com.guardiantech.aofgo.backend.request.note.StickyNoteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class StickyNoteService @Autowired constructor(
        val stickyNoteRepository: StickyNotesRepository
) {
    fun listAllNotes(): List<StickyNote> {
        return stickyNoteRepository.findAll().toList()
    }

    fun findByNoteId(id: String): StickyNote {
        return stickyNoteRepository.findByNoteId(id).get()
    }

    @Transactional
    fun addNote(request: StickyNoteRequest): StickyNote {
        val n = stickyNoteRepository.save(StickyNote(
                title = request.title,
                text = request.text
        ))
        return n
    }

    @Transactional
    fun deleteNote(request: StickyNoteRequest): List<StickyNote> {
        return stickyNoteRepository.deleteByNoteId(request.noteQueryId)
    }

    @Transactional
    fun editNote(request: StickyNoteRequest): StickyNote {

        val theNote = try {
            stickyNoteRepository.findByNoteId(request.noteQueryId).get()
        } catch (e: NoSuchElementException) {
            throw EntityNotFoundException("Note not found")
        }

        if (request.title != null) {
            theNote.title = request.title
        }
        if (request.text != null) {
            theNote.text = request.text
        }

        return stickyNoteRepository.save(theNote)
    }
}