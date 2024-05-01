package nsu.crackhash.worker.config

import com.fasterxml.jackson.databind.ObjectMapper
import nsu.crackhash.worker.infra.ManagerKafkaClient
import nsu.crackhash.worker.infra.ManagerRestApiClient
import nsu.crackhash.worker.infra.RestApiManagerConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.kafka.core.KafkaTemplate

@Configuration
class WorkerConfiguration {

    @Bean
    @ConditionalOnProperty("app.manager-url")
    fun managerRestApiClient(managerConfiguration: RestApiManagerConfiguration) =
        ManagerRestApiClient(managerConfiguration)

    @Bean
    @Primary
    @ConditionalOnProperty("app.kafka")
    fun managerKafkaClient(kafkaTemplate: KafkaTemplate<String, String>, objectMapper: ObjectMapper) =
        ManagerKafkaClient(kafkaTemplate, objectMapper)
}