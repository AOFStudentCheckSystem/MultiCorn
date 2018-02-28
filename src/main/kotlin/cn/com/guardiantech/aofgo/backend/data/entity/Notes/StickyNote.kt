package cn.com.guardiantech.aofgo.backend.data.entity.Notes

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class StickyNote(
        @Id
        @GeneratedValue
        var id: Long = 0,

        var title: String? = null,

        var text: String? = null,

        var noteId: String = System.currentTimeMillis().toString(36).toLowerCase()
)