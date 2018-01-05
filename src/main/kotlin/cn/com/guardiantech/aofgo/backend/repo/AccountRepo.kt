package cn.com.guardiantech.aofgo.backend.repo

import cn.com.guardiantech.aofgo.backend.data.entity.Account
import org.springframework.data.repository.CrudRepository
import java.util.*

interface AccountRepo : CrudRepository<Account, Long> {
    fun findById(id: Long): Optional<Account>
}