package cn.com.guardiantech.aofgo.backend

import cn.com.guardiantech.aofgo.backend.service.file.StorageProperties
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties::class)
@EnableTransactionManagement
class BackendApplication

fun main(args: Array<String>) {
    SpringApplication.run(BackendApplication::class.java, *args)
}
