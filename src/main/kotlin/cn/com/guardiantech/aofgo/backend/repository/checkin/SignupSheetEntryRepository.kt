package cn.com.guardiantech.aofgo.backend.repository.checkin

import cn.com.guardiantech.checkin.server.entity.EventGroup
import cn.com.guardiantech.checkin.server.entity.SignUpSheet
import cn.com.guardiantech.checkin.server.entity.SignupSheetEntry
import org.springframework.data.repository.CrudRepository
import java.util.*

/**
 * Created by Codetector on 2017/4/17.
 * Project backend
 */
interface SignupSheetEntryRepository : CrudRepository<SignupSheetEntry, String> {
    fun findBySheet(sheet: SignUpSheet): List<SignupSheetEntry>
    fun findByEventGroupAndSheet(group:EventGroup, sheet: SignUpSheet): Optional<SignupSheetEntry>
}