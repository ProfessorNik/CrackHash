package nsu.crackhash.manager

import nsu.crackhash.manager.config.WorkersConfiguration
import nsu.crackhash.manager.domain.port.ManagerCrackHashInfoRepository
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
    val workersConfiguration = context.getBean(WorkersConfiguration::class.java)
    val repository = context.getBean(ManagerCrackHashInfoRepository::class.java)
    System.err.println(workersConfiguration)
    System.err.println(repository::class.java)
}
