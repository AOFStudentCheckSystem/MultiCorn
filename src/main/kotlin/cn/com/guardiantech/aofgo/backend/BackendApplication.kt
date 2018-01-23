package cn.com.guardiantech.aofgo.backend

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
class BackendApplication

fun main(args: Array<String>) {
    SpringApplication.run(BackendApplication::class.java, *args)
}
