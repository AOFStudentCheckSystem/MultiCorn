package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.annotation.Controller
import cn.com.guardiantech.aofgo.backend.annotation.Handkerchief
import cn.com.guardiantech.aofgo.backend.annotation.RouteMapping
import io.vertx.core.http.HttpServerRequest

/**
 * Created by Codetector on 01/12/2017.
 * Project aofgo-backend
 */
@Controller
class AuthenticationController {
    @RouteMapping("/")
    fun test(@Handkerchief httpServerRequest: HttpServerRequest): String {
        return "Hello World"
    }
}