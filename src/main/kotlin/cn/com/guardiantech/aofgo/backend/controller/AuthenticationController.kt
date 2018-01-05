package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Session
import cn.com.guardiantech.aofgo.backend.request.authentication.AuthenticationRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.RegisterRequest
import cn.com.guardiantech.aofgo.backend.service.AuthenticationService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

/**
 * Created by Codetector on 01/12/2017.
 * Project aofgo-backend
 */
@Controller
@RequestMapping("/auth")
class AuthenticationController {
    val authenticationService = AuthenticationService()

//    @RequestMapping("/")
//    fun test(@Handkerchief httpServerRequest: HttpServerRequest): String {
//        return httpServerRequest.toString()
//    }

    @RequestMapping(path = ["/register"], method = [RequestMethod.POST])
    fun register(@RequestBody registerRequest: RegisterRequest) {
//        println("233 $registerRequest")
        authenticationService.register(registerRequest)
    }

    @RequestMapping(path = ["/auth"], method = [RequestMethod.POST])
    fun authenticate(@RequestBody authRequest: AuthenticationRequest): Session {
//        println("233 $authRequest")
        return authenticationService.authenticate(authRequest)
    }
}