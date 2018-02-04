package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.authentication.AuthContext
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Session
import cn.com.guardiantech.aofgo.backend.exception.BadRequestException
import cn.com.guardiantech.aofgo.backend.exception.RepositoryException
import cn.com.guardiantech.aofgo.backend.exception.UnauthorizedException
import cn.com.guardiantech.aofgo.backend.request.authentication.AuthenticationRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.SubjectRequest
import cn.com.guardiantech.aofgo.backend.service.auth.AuthenticationService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * Created by Codetector on 01/12/2017.
 * Project aofgo-backend
 */
@RestController
@RequestMapping("/auth")
class AuthenticationController @Autowired constructor(
        private val authenticationService: AuthenticationService
) {

    private val logger: Logger = LoggerFactory.getLogger(AuthenticationController::class.java)

    @PostMapping(path = ["/register"])
    fun register(@Valid @RequestBody registerRequest: SubjectRequest) = try {
        authenticationService.register(registerRequest)
    } catch (e: Throwable) {
        when (e) {
            is DataIntegrityViolationException -> {
                throw BadRequestException("Duplicate principal")
            }
            else -> {
                logger.error("Unexpected Error @ register", e)
                throw RepositoryException(e.message)
            }
        }
    }


    @RequestMapping(path = ["/auth"], method = [RequestMethod.POST])
    fun authenticate(authContext: AuthContext, @RequestBody(required = false) authRequest: AuthenticationRequest?): Session {
        return if (authContext.session != null) {
            authContext.session!!
        } else {
            try {
                authenticationService.authenticate(authRequest!!)
            } catch (e: Throwable) {
                if (e is UnauthorizedException) {
                    throw e
                }
                @Suppress("SimplifyNegatedBinaryExpression")
                if (!(e is UninitializedPropertyAccessException)) {

                }
                throw UnauthorizedException()
            }
        }
    }

    @GetMapping(path = ["/signOut"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun signOut(authContext: AuthContext) {
        authContext.session?.let { session ->
            authenticationService.invalidateSession(session.sessionKey)
        }
    }
}