package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.annotation.Controller
import cn.com.guardiantech.aofgo.backend.annotation.Handkerchief
import cn.com.guardiantech.aofgo.backend.annotation.RequestBody
import cn.com.guardiantech.aofgo.backend.annotation.RouteMapping
import cn.com.guardiantech.aofgo.backend.request.authentication.RegisterRequest
import cn.com.guardiantech.aofgo.backend.service.AuthenticationSerivce
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServerRequest

/**
 * Created by Codetector on 01/12/2017.
 * Project aofgo-backend
 */
@Controller
@RouteMapping("/auth")
class AuthenticationController {
    val authenticationSerivce = AuthenticationSerivce()

    @RouteMapping("/")
    fun test(@Handkerchief httpServerRequest: HttpServerRequest): String {
        return "Hello World"
    }

    @RouteMapping(path = "/register", method = [(HttpMethod.POST)])
    fun register(@RequestBody registerRequest: RegisterRequest) {
        authenticationSerivce.register(registerRequest)
    }
}