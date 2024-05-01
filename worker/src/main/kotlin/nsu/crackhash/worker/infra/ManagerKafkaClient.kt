package nsu.crackhash.worker.infra

import com.fasterxml.jackson.databind.ObjectMapper
import nsu.crackhash.worker.api.KafkaTopic
import nsu.crackhash.worker.api.ReportAboutCracking
import nsu.crackhash.worker.domain.usecase.ManagerGetaway
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class ManagerKafkaClient(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
) : ManagerGetaway {

    @Log
    override fun reportAboutCracking(reportAboutCracking: ReportAboutCracking) {
        kafkaTemplate.send(
            KafkaTopic.REPORTS_TOPIC_NAME,
            reportAboutCracking.requestId.toString(),
            objectMapper.writeValueAsString(reportAboutCracking)
        ).whenComplete { _, throwing ->
            if (throwing != null) {
                throw Exception("Request to Kafka failed")
            }
        }.join()
    }
}