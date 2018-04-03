package cn.com.guardiantech.aofgo.backend.repository.note

import cn.com.guardiantech.aofgo.backend.data.entity.Notes.StickyNote
import org.springframework.data.repository.CrudRepository
import java.util.*

interface StickyNoteRepository: CrudRepository<StickyNote, Long> {
    fun findById(id: Long): Optional<StickyNote>

    fun deleteById(id: Long): List<StickyNote>
}