package cn.com.guardiantech.aofgo.backend.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
class ImportBadConstraintException(message: String?) : ControllerException(message)