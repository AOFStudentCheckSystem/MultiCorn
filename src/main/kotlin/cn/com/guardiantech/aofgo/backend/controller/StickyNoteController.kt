package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.data.entity.Notes.StickyNote
import cn.com.guardiantech.aofgo.backend.exception.BadRequestException
import cn.com.guardiantech.aofgo.backend.exception.RepositoryException
import org.slf4j.Logger
import cn.com.guardiantech.aofgo.backend.request.note.StickyNoteRequest
import cn.com.guardiantech.aofgo.backend.service.note.StickyNoteService
import javassist.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/notes")
class StickyNoteController @Autowired constructor(
        val stickyNoteService: StickyNoteService
){
    private val logger: Logger = LoggerFactory.getLogger(StickyNoteController::class.java)

    @GetMapping("listAll")
    fun listAllNotes(): List<StickyNote> {
        return stickyNoteService.listAllNotes()
    }

    @GetMapping("/{id}")
    fun getStickyNoteByIdNumber(@PathVariable id: String): StickyNote = try {
        stickyNoteService.findByNoteId(id)
    } catch (e: NoSuchElementException) {
        throw NotFoundException("StickyNote Not Found")
    }

    @PutMapping("/")
    fun createStickyNote(@RequestBody @Valid noteRequest: StickyNoteRequest): StickyNote = try {
        stickyNoteService.addNote(noteRequest)
    } catch (e: IllegalArgumentException) {
        logger.error(e.message)
        throw BadRequestException(e.message)
    } catch (e: Throwable) {
        logger.error("StickyNote Saving Error:", e)
        throw RepositoryException("Failed to save StickyNote due to conflict")
    }
}