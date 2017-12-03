package cn.com.guardiantech.aofgo.backend.routing.testPackage

import cn.com.guardiantech.aofgo.backend.annotation.Controller
import cn.com.guardiantech.aofgo.backend.annotation.RouteMapping
import io.vertx.core.http.HttpMethod

/**
 * Created by Codetector on 03/12/2017.
 * Project aofgo-backend
 */
@Controller
@RouteMapping(path = "/test", method = [])
class ControllerTest {
    @RouteMapping(path = "/subPath", method = [HttpMethod.GET])
    fun testHandler(): String {
        return ""
    }
}