package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Session
import cn.com.guardiantech.aofgo.backend.request.authentication.AuthenticationRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.RegisterRequest
import cn.com.guardiantech.aofgo.backend.service.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Created by Codetector on 01/12/2017.
 * Project aofgo-backend
 */
@Controller
@RequestMapping("/auth")
class AuthenticationController @Autowired constructor(
        val authenticationService: AuthenticationService
) {

//    @RequestMapping("/")
//    fun test(@Handkerchief httpServerRequest: HttpServerRequest): String {
//        return httpServerRequest.toString()
//    }

    @PostMapping(path = ["/register"])
    fun register(@RequestBody registerRequest: RegisterRequest) {
//        println("233 $registerRequest")
        try {
            authenticationService.register(registerRequest)
        } catch (e: NoSuchElementException) {
            throw e
        } catch (e: Throwable) {
            throw e
        }
    }

    @PostMapping(path = ["/auth"])
    fun authenticate(@RequestBody authRequest: AuthenticationRequest): Session {
//        println("233 $authRequest")
        try {
            return authenticationService.authenticate(authRequest)
        } catch (e: Throwable) {
            throw e
        }
    }
}