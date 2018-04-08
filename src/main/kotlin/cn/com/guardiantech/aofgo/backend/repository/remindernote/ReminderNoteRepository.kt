package cn.com.guardiantech.aofgo.backend.repository.remindernote

import cn.com.guardiantech.aofgo.backend.data.entity.remindernote.ReminderNote
import org.springframework.data.repository.CrudRepository

/**
 * Created by Codetector on 2018/04/08.
 * Project AOFGoBackend
 */
interface ReminderNoteRepository: CrudRepository<ReminderNote, Long> {

}