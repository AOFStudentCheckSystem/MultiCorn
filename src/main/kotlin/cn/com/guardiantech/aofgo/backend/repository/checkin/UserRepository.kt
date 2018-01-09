package cn.com.guardiantech.aofgo.backend.repository.checkin

import cn.com.guardiantech.aofgo.backend.data.entity.Account
import org.springframework.data.repository.CrudRepository
import java.util.*

/**
 * Created by Codetector on 2017/4/6.
 * Project backend
 */
interface UserRepository : CrudRepository<Account, Long> {
    fun findByEmailIgnoreCase(email: String): Optional<Account>

    //Search Functions
    fun findByEmailStartingWithIgnoreCase(emailBegin: String): List<Account>
}