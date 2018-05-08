package cn.com.guardiantech.aofgo.backend.repository

import cn.com.guardiantech.aofgo.backend.data.entity.Guardian
import cn.com.guardiantech.aofgo.backend.data.entity.GuardianType
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface GuardianRepository : CrudRepository<Guardian, Long> {
    fun findById(id: Long): Optional<Guardian>
    @Query("select g from #{#entityName} g WHERE g.guardianAccount.id = ?1 AND g.relation = ?2")
    fun findByAccountIdAndRelation(id: Long, relation: GuardianType): Optional<Guardian>
}