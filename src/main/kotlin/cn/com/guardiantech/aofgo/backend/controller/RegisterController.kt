package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.exception.AccountVerificationFailedException
import cn.com.guardiantech.aofgo.backend.request.authentication.EmailAddressSubmissionRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.registraion.EmailValidationResult
import cn.com.guardiantech.aofgo.backend.request.authentication.registraion.RegistrationRequest
import cn.com.guardiantech.aofgo.backend.service.EmailVerificationService
import cn.com.guardiantech.aofgo.backend.service.auth.RegistrationService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * Created by dedztbh on 18-4-15.
 * Project AOFGoBackend
 */

@RestController
@RequestMapping("/register")
class RegisterController @Autowired constructor(
        private val registerService: RegistrationService,
        private val emailVerificationService: EmailVerificationService
) {

    private val logger: Logger = LoggerFactory.getLogger(RegisterController::class.java)

    @PostMapping(path = ["/valid/email"])
    fun checkEmailAddressValidity(@RequestBody @Valid emailRequest: EmailAddressSubmissionRequest): EmailValidationResult {
        return registerService.checkEmailAddressValidity(emailRequest.address)
    }

    @PutMapping(path = ["/"])
    fun registerUserWithEmail(@RequestBody @Valid registerRequest: RegistrationRequest) {
        return registerService.registerUserWithEmail(registerRequest)
    }

    @GetMapping(path = ["/verify/{code}"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun verifyAccount(@PathVariable token: String) {
        try {
            if (emailVerificationService.let { it.activateAccount(it.findValidationFromToken(token)) }) {
                throw Throwable()
            }
        } catch (e: Throwable) {
            throw AccountVerificationFailedException()
        }
    }
}