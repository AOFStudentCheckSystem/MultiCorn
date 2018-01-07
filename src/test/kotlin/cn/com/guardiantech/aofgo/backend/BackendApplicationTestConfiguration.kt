package cn.com.guardiantech.aofgo.backend

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * Created by dedztbh on 1/6/18.
 * Project AOFGoBackend
 */
@Configuration
@ComponentScan("cn.com.guardiantech.aofgo.backend")
@ImportAutoConfiguration(classes = [MailSenderAutoConfiguration::class])
class BackendApplicationTestConfiguration {

}