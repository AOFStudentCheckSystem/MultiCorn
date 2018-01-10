package cn.com.guardiantech.aofgo.backend.configuration

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
}