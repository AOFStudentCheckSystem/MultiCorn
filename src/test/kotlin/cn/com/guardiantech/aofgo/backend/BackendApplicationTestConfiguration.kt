package cn.com.guardiantech.aofgo.backend

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc

/**
 * Created by dedztbh on 1/6/18.
 * Project AOFGoBackend
 */
@Configuration
@ComponentScan("cn.com.guardiantech.aofgo.backend")
@ImportAutoConfiguration(classes = [MailSenderAutoConfiguration::class])
@EnableWebMvc
class BackendApplicationTestConfiguration {

}