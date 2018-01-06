package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.authentication.AuthContext
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Session
import cn.com.guardiantech.aofgo.backend.request.authentication.AuthenticationRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.RegisterRequest
import cn.com.guardiantech.aofgo.backend.service.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by Codetector on 01/12/2017.
 * Project aofgo-backend
 */
@RestController
@RequestMapping("/auth")
class AuthenticationController @Autowired constructor(
        private val authenticationService: AuthenticationService
) {

    @PostMapping(path = ["/register"])
    fun register(@RequestBody registerRequest: RegisterRequest) {
        try {
            authenticationService.register(registerRequest)
        } catch (e: NoSuchElementException) {
            throw e
        } catch (e: Throwable) {
            throw e
        }
    }

    @PostMapping(path = ["/auth"])
    fun authenticate(authContext: AuthContext, @RequestBody authRequest: AuthenticationRequest): Session {
        return if (authContext.session != null) {
            authContext.session!!
        } else {
            try {
                authenticationService.authenticate(authRequest)
            } catch (e: Throwable) {
                throw e
            }
        }
    }
}