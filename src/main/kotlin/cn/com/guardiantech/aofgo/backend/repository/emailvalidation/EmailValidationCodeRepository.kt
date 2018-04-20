package cn.com.guardiantech.aofgo.backend.repository.emailvalidation

import cn.com.guardiantech.aofgo.backend.data.entity.emailvalidation.EmailValidationCode
import org.springframework.data.repository.CrudRepository

interface EmailValidationCodeRepository: CrudRepository<EmailValidationCode, Long> {
    fun findByCode(code: String): EmailValidationCode?
}