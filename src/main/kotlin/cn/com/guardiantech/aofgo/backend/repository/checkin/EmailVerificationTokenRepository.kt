package cn.com.guardiantech.aofgo.backend.repository.checkin

import cn.com.guardiantech.checkin.server.entity.authentication.EmailVerificationToken
import org.springframework.data.repository.CrudRepository
import java.util.*

/**
 * Created by Codetector on 2017/4/17.
 * Project backend
 */
interface EmailVerificationTokenRepository : CrudRepository<EmailVerificationToken, String> {

    fun findById(id: String): Optional<EmailVerificationToken>
    fun findByEmailIgnoreCase(email: String): Optional<EmailVerificationToken>

}