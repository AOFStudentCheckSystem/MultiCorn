package cn.com.guardiantech.aofgo.backend.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Created by Codetector on 2017/5/2.
 * Project backend
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
class PermissionDeniedException(message: String?): ControllerException(message)