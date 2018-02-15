package cn.com.guardiantech.aofgo.backend.authentication

import cn.com.guardiantech.aofgo.backend.annotation.Require
import cn.com.guardiantech.aofgo.backend.service.auth.AuthenticationService
import cn.com.guardiantech.aofgo.backend.service.auth.AuthorizationService
import org.slf4j.LoggerFactory
import org.springframework.aop.support.AopUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
class AuthContextConfiguration : WebMvcConfigurerAdapter(), ApplicationContextAware {

    @Autowired
    lateinit var authorizationService: AuthorizationService
    @field:Value("\${auth.disableAuth:false}")
    var disableAuth: Boolean? = null


    companion object {
        private val logger = LoggerFactory.getLogger(AuthContextConfiguration::class.java)
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        val declaredPermissions: MutableSet<String> = hashSetOf()
        applicationContext.beanDefinitionNames.forEach { bName ->
            val obj: Any = applicationContext.getBean(bName)

            var clazz: Class<*> = obj.javaClass
            if (AopUtils.isAopProxy(obj)) {
                clazz = AopUtils.getTargetClass(obj)
            }
            if (clazz.isAnnotationPresent(Require::class.java)) {
                (clazz.getAnnotation(Require::class.java) as Require).permissions.forEach {
                    declaredPermissions.add(it)
                }
            }
            clazz.declaredMethods.forEach { m ->
                if (m.isAnnotationPresent(Require::class.java)) {
                    val require = m.getAnnotation(Require::class.java) as Require
                    require.permissions.forEach {
                        declaredPermissions.add(it)
                    }
                }
            }
        }
        logger.info("Detected Permissions: $declaredPermissions")
        authorizationService.initializePermissions(declaredPermissions)
    }

    @Autowired
    @Lazy
    private lateinit var authContextHandlerInterceptor: AuthContextHandlerInterceptor

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(AuthContextMethodArgumentResolver())
    }

    override fun addInterceptors(registry: InterceptorRegistry?) {
        registry?.addInterceptor(authContextHandlerInterceptor)
    }

    @Bean
    fun authContextHandlerInterceptor(authenticationService: AuthenticationService): AuthContextHandlerInterceptor {
        return AuthContextHandlerInterceptor(authenticationService, disableAuth!!)
    }
}