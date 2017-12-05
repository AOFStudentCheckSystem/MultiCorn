package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.annotation.Controller
import cn.com.guardiantech.aofgo.backend.annotation.RouteMapping

/**
 * Created by Codetector on 01/12/2017.
 * Project aofgo-backend
 */
@Controller
class AuthenticationController {
    @RouteMapping("/")
    fun test(): String {
        return "Hello World"
    }
}