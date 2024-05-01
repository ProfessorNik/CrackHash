package nsu.crackhash.manager.config

import org.springframework.boot.context.properties.ConfigurationProperties

typealias WorkerUrl = String

@ConfigurationProperties(prefix = "app")
data class ApplicationConfigurationProperties(
    val ttl: Long
)

@ConfigurationProperties(prefix = "app.rest")
data class RestConfigurationProperties(val workers: List<WorkerUrl>)

@ConfigurationProperties(prefix = "app.kafka-topics")
data class KafkaTopicsConfigurationProperties(
    val quantityWorkers: Int
)
