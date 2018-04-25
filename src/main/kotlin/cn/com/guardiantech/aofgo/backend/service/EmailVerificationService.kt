package cn.com.guardiantech.aofgo.backend.service

import cn.com.guardiantech.aofgo.backend.data.entity.Account
import cn.com.guardiantech.aofgo.backend.data.entity.AccountStatus
import cn.com.guardiantech.aofgo.backend.data.entity.emailvalidation.EmailValidationCode
import cn.com.guardiantech.aofgo.backend.exception.EntityNotFoundException
import cn.com.guardiantech.aofgo.backend.repository.emailvalidation.EmailValidationCodeRepository
import cn.com.guardiantech.aofgo.backend.util.keyGenerator.Base32RandomString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.SecureRandom

/**
 * Note: Code is Token !!
 */

@Service
class EmailVerificationService @Autowired constructor(
        private val emailValidationCodeRepository: EmailValidationCodeRepository
) {

    private val randomGen = Base32RandomString(20, SecureRandom())

    @Transactional
    fun assignEmailValidationCode(account: Account): EmailValidationCode {
        var generatedCode: EmailValidationCode?
        var code: String
        do {
            code = randomGen.nextString()
            generatedCode = emailValidationCodeRepository.findByCode(code)
        } while (generatedCode !== null)

        generatedCode = emailValidationCodeRepository.save(
                EmailValidationCode(
                        owner = account,
                        code = code
                )
        )

        return generatedCode!!
    }

    fun findValidationFromToken(code: String): EmailValidationCode {
        emailValidationCodeRepository.findByCode(code)?.let {
            return it
        }
        throw EntityNotFoundException("Validation Token does not exists")
    }

    @Transactional
    fun activateAccount(code: EmailValidationCode): Boolean {
        if (code.owner.accountStatus == AccountStatus.VERIFICATION) {
            code.owner.accountStatus = AccountStatus.ACTIVE
            emailValidationCodeRepository.delete(emailValidationCodeRepository.save(code))
            return true
        }
        return false
    }
}