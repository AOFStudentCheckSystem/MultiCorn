package cn.com.guardiantech.aofgo.backend.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Created by DE_DZ_TBH on 2017/3/28.
 * All rights reserved.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
class UnauthorizedException: RuntimeException()