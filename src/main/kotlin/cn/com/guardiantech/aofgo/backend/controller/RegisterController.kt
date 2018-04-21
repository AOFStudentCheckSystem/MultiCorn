package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.PrincipalType
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Subject
import cn.com.guardiantech.aofgo.backend.exception.BadRequestException
import cn.com.guardiantech.aofgo.backend.request.authentication.EmailAddressSubmissionRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.SubjectRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.registraion.EmailValidationResult
import cn.com.guardiantech.aofgo.backend.request.authentication.registraion.UsernameValidationRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.registraion.UsernameValidationResponse
import cn.com.guardiantech.aofgo.backend.service.auth.RegistrationService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * Created by dedztbh on 18-4-15.
 * Project AOFGoBackend
 */

@RestController
@RequestMapping("/register")
class RegisterController @Autowired constructor(
        private val registerService: RegistrationService
) {

    private val logger: Logger = LoggerFactory.getLogger(RegisterController::class.java)

    @PostMapping(path = ["/valid/email"])
    fun checkEmailAddressValidity(@RequestBody @Valid emailRequest: EmailAddressSubmissionRequest): EmailValidationResult {
        return registerService.checkEmailAddressValidity(emailRequest.address)
    }

    @PostMapping(path = ["/valid/username"])
    fun checkUsernameValidity(@RequestBody @Valid usernameValidationRequest: UsernameValidationRequest): UsernameValidationResponse{
        val result = registerService.checkUsernameValidity(username = usernameValidationRequest.username)
        return UsernameValidationResponse(username = usernameValidationRequest.username,
                result = result)
    }

    @PutMapping(path = ["/"])
    fun bindingRegisterSubject(@RequestBody @Valid registerRequest: SubjectRequest): Subject {
        if (registerRequest.principal.type !== PrincipalType.EMAIL) throw BadRequestException("Please register with email")
        return registerService.bindingRegisterSubject(registerRequest)
    }
}