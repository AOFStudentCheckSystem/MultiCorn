package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.data.entity.Notes.StickyNote
import cn.com.guardiantech.aofgo.backend.exception.BadRequestException
import cn.com.guardiantech.aofgo.backend.exception.ControllerException
import cn.com.guardiantech.aofgo.backend.exception.EntityNotFoundException
import cn.com.guardiantech.aofgo.backend.exception.RepositoryException
import cn.com.guardiantech.aofgo.backend.request.note.StickyNoteAddRequest
import org.slf4j.Logger
import cn.com.guardiantech.aofgo.backend.request.note.StickyNoteEditRequest
import cn.com.guardiantech.aofgo.backend.service.note.StickyNoteService
import javassist.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
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
    fun getStickyNoteByIdNumber(@PathVariable id: Long): StickyNote = try {
        stickyNoteService.findById(id)
    } catch (e: NoSuchElementException) {
        throw NotFoundException("StickyNote Not Found")
    }

    @PutMapping("/add")
    fun createStickyNote(@RequestBody @Valid noteAddRequest: StickyNoteAddRequest): StickyNote = try {
        stickyNoteService.addNote(noteAddRequest)
    } catch (e: IllegalArgumentException) {
        logger.error(e.message)
        throw BadRequestException(e.message)
    } catch (e: Throwable) {
        logger.error("StickyNote Saving Error:", e)
        throw RepositoryException("Failed to save StickyNote due to conflict")
    }

    @RequestMapping(path = ["/{id}"], method = [RequestMethod.DELETE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeStickyNote(@PathVariable("id") id: Long) {
        try {
            stickyNoteService.deleteNote(id)
        } catch (e: NoSuchElementException) {
            throw BadRequestException(e.message)
        }
    }

    @PostMapping("/edit")
    fun editStickyNote(@RequestBody @Valid editRequest: StickyNoteEditRequest) = try {
        stickyNoteService.editNote(editRequest)
    } catch (e: ControllerException) {
        throw e
    } catch (e: NoSuchElementException) {
        throw EntityNotFoundException("StickyNote Not Found")
    } catch (e: Throwable) {
        logger.error("Failed while editing StickyNote", e)
        throw RepositoryException("Failed to edit StickyNote")
    }
}