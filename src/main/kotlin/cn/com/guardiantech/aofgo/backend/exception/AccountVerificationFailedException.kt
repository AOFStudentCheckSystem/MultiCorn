package cn.com.guardiantech.aofgo.backend.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Created by dedztbh on 4/25/18.
 * Project AOFGoBackend
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
class AccountVerificationFailedException(message: String? = "Cannot active account") : ControllerException(message)