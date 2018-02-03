package cn.com.guardiantech.aofgo.backend.configuration

import org.apache.catalina.connector.Connector
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
@EnableWebMvc
class CorsConfiguration : WebMvcConfigurerAdapter() {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("Authorization", "Content-Type", "Content-Length")
                .allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE")
//        super.addCorsMappings(registry)
    }

    @Bean
    fun tomcatEmbeddedServletContainerFactory(): TomcatEmbeddedServletContainerFactory {
        return object : TomcatEmbeddedServletContainerFactory() {
           override fun customizeConnector(connector: Connector) {
                super.customizeConnector(connector)
                connector.parseBodyMethods = "POST,PUT,DELETE"
            }
        }
    }
}