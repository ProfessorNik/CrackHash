package nsu.crackhash.manager.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
@ConditionalOnProperty("app.kafka-topics.quantity-workers")
class KafkaTopicsConfiguration {

    companion object {
        const val TASKS_TOPIC_NAME = "tasks"
        const val REPORTS_TOPIC_NAME = "reports"
    }

    @Bean
    fun topicTasks(kafkaTopicsConfigurationProperties: KafkaTopicsConfigurationProperties) =
        TopicBuilder.name(TASKS_TOPIC_NAME)
            .partitions(kafkaTopicsConfigurationProperties.quantityWorkers)
            .replicas(1)
            .build()

    @Bean
    fun topicReports() =
        TopicBuilder.name(REPORTS_TOPIC_NAME)
            .partitions(1)
            .replicas(1)
            .build()
}