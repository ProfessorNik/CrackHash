package nsu.crackhash.worker.infra

import org.springframework.boot.context.properties.ConfigurationProperties

typealias MangerUrl = String

@ConfigurationProperties(prefix = "app")
data class RestApiManagerConfiguration(
    val managerUrl: MangerUrl
)
