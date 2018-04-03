package cn.com.guardiantech.aofgo.backend.request.note

data class StickyNoteEditRequest (
        val title: String?,
        val text: String?,
        val id: Long
)