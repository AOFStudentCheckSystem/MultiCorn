package cn.com.guardiantech.aofgo.backend.repository.veracross

import cn.com.guardiantech.aofgo.backend.data.entity.veracross.VeracrossAssignment
import org.springframework.data.repository.CrudRepository
import java.util.*

interface VeracrossAssignmentRepo : CrudRepository<VeracrossAssignment, Long> {
    fun findById(id: Long): Optional<VeracrossAssignment>
}