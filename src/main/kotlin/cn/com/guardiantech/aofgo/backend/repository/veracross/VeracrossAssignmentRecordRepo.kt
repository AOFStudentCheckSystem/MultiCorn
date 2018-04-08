package cn.com.guardiantech.aofgo.backend.repository.veracross

import cn.com.guardiantech.aofgo.backend.data.entity.veracross.VeracrossAssignmentRecord
import org.springframework.data.repository.CrudRepository
import java.util.*

interface VeracrossAssignmentRecordRepo : CrudRepository<VeracrossAssignmentRecord, Long> {
    fun findById(id: Long): Optional<VeracrossAssignmentRecord>
}