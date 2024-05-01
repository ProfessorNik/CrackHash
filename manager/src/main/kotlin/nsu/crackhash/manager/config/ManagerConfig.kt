package nsu.crackhash.manager.config

import com.fasterxml.jackson.databind.ObjectMapper
import nsu.crackhash.manager.infra.data.ManagerCrackHashInfoDao
import nsu.crackhash.manager.infra.data.ManagerCrackHashInfoMongoRepository
import nsu.crackhash.manager.infra.data.WorkerTaskDao
import nsu.crackhash.manager.infra.web.WorkerKafkaClient
import nsu.crackhash.manager.infra.web.WorkerRestApiClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.kafka.core.KafkaTemplate


@Configuration
class ManagerConfig {

//    @Bean
//    fun managerCrackHashInfoMapRepository() =
//        ManagerCrackHashInfoMapRepository()

    @Bean
    @Primary
    fun managerCrackHashInfoMongoRepository(managerCrackHashInfoDao: ManagerCrackHashInfoDao) =
        ManagerCrackHashInfoMongoRepository(managerCrackHashInfoDao)

    @Bean
    @ConditionalOnProperty("app.rest.workers")
    fun workerRestApiClient(
        restConfigurationProperties: RestConfigurationProperties
    ) = WorkerRestApiClient(
        restConfigurationProperties
    )

    @Bean
    @Primary
    @ConditionalOnProperty("app.kafka-topics.quantity-workers")
    fun workerKafkaClient(
        kafkaTopicsConfigurationProperties: KafkaTopicsConfigurationProperties,
        kafkaTemplate: KafkaTemplate<String, String>,
        taskAssignRequestDao: WorkerTaskDao,
        objectMapper: ObjectMapper
    ) = WorkerKafkaClient(
        kafkaTopicsConfigurationProperties,
        kafkaTemplate,
        taskAssignRequestDao,
        objectMapper
    )
}