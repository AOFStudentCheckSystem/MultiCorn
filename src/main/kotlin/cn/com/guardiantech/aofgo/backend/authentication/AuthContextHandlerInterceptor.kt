package cn.com.guardiantech.aofgo.backend.authentication

import cn.com.guardiantech.aofgo.backend.annotation.Require
import cn.com.guardiantech.aofgo.backend.exception.UnauthorizedException
import cn.com.guardiantech.aofgo.backend.service.auth.AuthenticationService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.lang.Exception
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

open class AuthContextHandlerInterceptor constructor(
        private val authenticationService: AuthenticationService,
        var disableAuth: Boolean = false
) : HandlerInterceptor {

    private val logger: Logger = LoggerFactory.getLogger(AuthContextHandlerInterceptor::class.java)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse?, handler: Any?): Boolean {

        // Implement AuthContext
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null && authHeader.isNotBlank()) {
            val session = authenticationService.authenticateSession(authHeader)
            if (session != null) {
                AuthContext.currentContextInternal = AuthContext(session)
            } else {
                AuthContext.clear()
            }
        }

        val authCtx = AuthContext.currentContext

        if (handler is HandlerMethod) {
            if (checkRequireExists(handler)) {

                // This should be guaranteed, as checkRequireExists() has already checked the preconditions
                //val require = getNearestRequire(handler)!!

                val permissions = getMergedPermissions(handler)

                if (authCtx.isAuthenticated()) {
                    if (!authenticationService.verifySessionPermission(authHeader, permissions) && !disableAuth) {
                        throw UnauthorizedException("Missing Permission")
                    }
                } else {
                    throw UnauthorizedException("FUCK YOU, go LOGIN")
                }
            }
        }
        return true
    }

    private fun checkRequireExists(handlerMethod: HandlerMethod): Boolean {
        return handlerMethod.hasMethodAnnotation(Require::class.java) || handlerMethod.method.declaringClass.isAnnotationPresent(Require::class.java)
    }

    private fun getNearestRequire(handlerMethod: HandlerMethod): Require? {
        if (handlerMethod.hasMethodAnnotation(Require::class.java)) {
            return handlerMethod.getMethodAnnotation(Require::class.java)
        } else if (handlerMethod.method.declaringClass.isAnnotationPresent(Require::class.java)) {
            return handlerMethod.method.declaringClass.getDeclaredAnnotation(Require::class.java)
        }
        return null
    }

    private fun getMergedPermissions(handlerMethod: HandlerMethod): Array<String> {
        if (handlerMethod.hasMethodAnnotation(Require::class.java)) {
            val permissions = handlerMethod.getMethodAnnotation(Require::class.java).permissions.toMutableSet()
            if (handlerMethod.method.declaringClass.isAnnotationPresent(Require::class.java)) {
                permissions.addAll(handlerMethod.method.declaringClass.getDeclaredAnnotation(Require::class.java).permissions)
            }
            return permissions.toTypedArray()
        } else if (handlerMethod.method.declaringClass.isAnnotationPresent(Require::class.java)) {
            return handlerMethod.method.declaringClass.getDeclaredAnnotation(Require::class.java).permissions
        }
        return arrayOf()
    }


    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse?, handler: Any?, modelAndView: ModelAndView?) {
        AuthContext.clear()
        logger.trace("Clearing AuthContext")
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse?, handler: Any?, ex: Exception?) {
        AuthContext.clear()
        logger.trace("Clearing AuthContext")
    }
}