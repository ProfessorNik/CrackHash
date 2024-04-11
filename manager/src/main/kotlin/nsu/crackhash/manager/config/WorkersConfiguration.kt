package nsu.crackhash.manager.config

import org.springframework.boot.context.properties.ConfigurationProperties

typealias WorkerUrl = String

@ConfigurationProperties(prefix = "app")
data class WorkersConfiguration(
    val workers: List<WorkerUrl>
)

