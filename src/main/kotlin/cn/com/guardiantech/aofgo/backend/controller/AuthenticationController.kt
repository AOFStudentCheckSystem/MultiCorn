package cn.com.guardiantech.aofgo.backend.controller

import cn.codetector.jet.controller.annotation.Controller
import cn.codetector.jet.controller.annotation.Handkerchief
import cn.codetector.jet.controller.annotation.RequestBody
import cn.codetector.jet.controller.annotation.RouteMapping
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
        return httpServerRequest.toString()
    }

    @RouteMapping(path = "/register", method = [(HttpMethod.POST)])
    fun register(@RequestBody registerRequest: RegisterRequest) {
        authenticationSerivce.register(registerRequest)
    }
}