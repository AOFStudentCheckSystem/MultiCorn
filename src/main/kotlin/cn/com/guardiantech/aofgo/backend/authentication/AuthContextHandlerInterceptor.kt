package cn.com.guardiantech.aofgo.backend.authentication

import cn.com.guardiantech.aofgo.backend.annotation.Require
import cn.com.guardiantech.aofgo.backend.service.AuthenticationService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.lang.Exception
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

open class AuthContextHandlerInterceptor constructor(
        private val authenticationService: AuthenticationService
): HandlerInterceptor {

    private val logger: Logger = LoggerFactory.getLogger(AuthContextHandlerInterceptor::class.java)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

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
            if (handler.hasMethodAnnotation(Require::class.java)) {
                val require = handler.getMethodAnnotation(Require::class.java)
                // Todo implement permission check
                if (authCtx.isAuthenticated()) {

                } else {
                    return false
                }
            }
        }
        return true
    }

    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView) {
        AuthContext.clear()
        logger.debug("Clearing AuthContext")
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception) {
        AuthContext.clear()
        logger.debug("Clearing AuthContext")
    }
}