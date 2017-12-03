package cn.com.guardiantech.aofgo.backend.`interface`

import io.vertx.ext.web.Router

/**
 * Created by Codetector on 02/12/2017.
 * Project aofgo-backend
 */
interface WebController {
    fun configureRoutes(router: Router)
}