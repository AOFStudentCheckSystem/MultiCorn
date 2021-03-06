package cn.com.guardiantech.aofgo.backend.authentication

import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class AuthContextMethodArugmentResolver :HandlerMethodArgumentResolver{
    override fun supportsParameter(parameter: MethodParameter): Boolean = parameter.parameterType == AuthContext::class.java

    override fun resolveArgument(parameter: MethodParameter?, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest?, binderFactory: WebDataBinderFactory?): Any = AuthContext.currentContext
}