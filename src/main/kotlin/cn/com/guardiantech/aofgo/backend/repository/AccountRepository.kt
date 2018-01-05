package cn.com.guardiantech.aofgo.backend.repository

import cn.com.guardiantech.aofgo.backend.data.entity.Account
import org.springframework.data.repository.CrudRepository
import java.util.*

interface AccountRepository : CrudRepository<Account, Long> {
    fun findById(id: Long): Optional<Account>
}