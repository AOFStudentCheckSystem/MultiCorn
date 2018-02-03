package cn.com.guardiantech.aofgo.backend.authentication

import cn.com.guardiantech.aofgo.backend.annotation.Require
import cn.com.guardiantech.aofgo.backend.service.auth.AuthenticationService
import org.slf4j.LoggerFactory
import org.springframework.aop.support.AopUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
class AuthContextConfiguration: WebMvcConfigurerAdapter(), ApplicationContextAware {

    companion object {
        private val logger = LoggerFactory.getLogger(AuthContextConfiguration::class.java)
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        applicationContext.beanDefinitionNames.forEach { bName ->
            val obj: Any = applicationContext.getBean(bName)

            var clazz: Class<*> = obj.javaClass
            if (AopUtils.isAopProxy(obj)) {
                clazz = AopUtils.getTargetClass(obj)
            }
            if (clazz.isAnnotationPresent(Require::class.java)) {
                (clazz.getAnnotation(Require::class.java) as Require).permissions.forEach {
                    SharedAuthConfiguration.declaredPermissions.add(it)
                }
            }
            clazz.declaredMethods.forEach { m ->
                if (m.isAnnotationPresent(Require::class.java)) {
                    val require = m.getAnnotation(Require::class.java) as Require
                    require.permissions.forEach {
                        SharedAuthConfiguration.declaredPermissions.add(it)
                    }
                }
            }
        }

        logger.info("Detected Permissions: ${SharedAuthConfiguration.declaredPermissions}")
    }

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