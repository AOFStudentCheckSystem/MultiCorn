package cn.com.guardiantech.aofgo.backend.authentication

import cn.com.guardiantech.aofgo.backend.service.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
class AuthContextConfiguration: WebMvcConfigurerAdapter() {

    @Autowired
    @Lazy
    private lateinit var authContextHandlerInterceptor:AuthContextHandlerInterceptor

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(AuthContextMethodArugmentResolver())
    }

    override fun addInterceptors(registry: InterceptorRegistry?) {
        registry?.addInterceptor(authContextHandlerInterceptor)
    }

    @Bean
    fun authContextHandlerInterceptor(authenticationService: AuthenticationService): AuthContextHandlerInterceptor {
        return AuthContextHandlerInterceptor(authenticationService)
    }
}