package cn.com.guardiantech.aofgo.backend.repository.checkin

import cn.com.guardiantech.aofgo.backend.data.entity.checkin.SignUpSheet
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.transaction.Transactional

/**
 * Created by Codetector on 2017/4/7.
 * Project backend
 */
interface SignUpSheetRepository : CrudRepository<SignUpSheet, Long> {
    override fun findAll(): List<SignUpSheet>
    fun findByStatus(status: Int): List<SignUpSheet>
    fun findById(id: Long): Optional<SignUpSheet>

    @Modifying
    @Transactional
    fun removeById(id: Long): Long

}