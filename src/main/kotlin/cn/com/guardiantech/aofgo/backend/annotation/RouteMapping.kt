package cn.com.guardiantech.aofgo.backend.annotation

import io.vertx.core.http.HttpMethod

/**
 * Created by Codetector on 02/12/2017.
 * Project aofgo-backend
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(allowedTargets = [AnnotationTarget.FUNCTION, AnnotationTarget.CLASS])
annotation class RouteMapping(val path: String, val method: Array<HttpMethod> = [])