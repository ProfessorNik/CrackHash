package nsu.crackhash.manager

import nsu.crackhash.manager.config.KafkaTopicsConfigurationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableConfigurationProperties
@EnableAsync
@EnableScheduling
class ManagerApplication

fun main(args: Array<String>) {
    val context = runApplication<ManagerApplication>(*args)
    val properties = context.getBean(KafkaTopicsConfigurationProperties::class.java)
    System.err.println(properties)
}
