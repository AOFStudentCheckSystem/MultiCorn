package cn.com.guardiantech.aofgo.backend.request.note

data class StickyNoteRequest (
        val title: String?,
        val text: String?,
        val noteId: String
)