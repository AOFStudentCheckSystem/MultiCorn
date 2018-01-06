package cn.com.guardiantech.aofgo.backend.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Created by dedztbh on 1/6/18.
 * Project AOFGoBackend
 */

@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundException(message: String?): ControllerException(message)