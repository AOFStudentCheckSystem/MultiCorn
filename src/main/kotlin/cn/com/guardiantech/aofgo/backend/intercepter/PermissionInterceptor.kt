package cn.com.guardiantech.aofgo.backend.intercepter

//import cn.codetector.jet.routing.RoutingContext
//import cn.codetector.jet.routing.interceptor.InterceptorHandler
//import cn.codetector.jet.routing.request.HttpRequest
//import cn.com.guardiantech.aofgo.backend.annotation.Require
//import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Session
//import cn.com.guardiantech.aofgo.backend.util.entityManager
//import java.lang.reflect.Method
//
//class PermissionInterceptor : InterceptorHandler {
//    override fun cleanUpHandler(handlerMethod: Method, httpRequest: HttpRequest, routingContext: RoutingContext) {
//    }
//
//    override fun postHandle(handlerMethod: Method, httpRequest: HttpRequest, routingContext: RoutingContext) {
//    }
//
//    override fun preHandle(handlerMethod: Method, httpRequest: HttpRequest, routingContext: RoutingContext): Boolean {
//        if (handlerMethod.isAnnotationPresent(Require::class.java)) {
//            val sessionKey = httpRequest.getHeader("Authorization")
//            if (sessionKey.isNullOrBlank()) {
//                fail(httpRequest, "Invalid Session Key")
//                return false
//            }
//            val subject = entityManager {
//                val results = it.createQuery("FROM Session S WHERE S.sessionKey = :sessionKey")
//                        .setParameter("sessionKey", sessionKey)
//                        .resultList
//                if (results.size != 1) {
//                    fail(httpRequest, "Not A User Found")
//                    return@entityManager null
//                }
//                (results[0] as Session).subject
//            }
//            if (subject === null) {
//                return false
//            }
//            val requiredPermissions = handlerMethod.getDeclaredAnnotation(Require::class.java).permissions
//            requiredPermissions.forEach {
//                // TODO: Permissions
//            }
//        }
//        return true
//    }
//
//    private fun fail(httpRequest: HttpRequest, msg: String) {
//        httpRequest.rawRequest.response().setStatusCode(401).end(msg)
//    }
//}