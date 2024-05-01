package nsu.crackhash.manager.infra.web

import com.fasterxml.jackson.databind.ObjectMapper
import nsu.crackhash.manager.api.CrackHashWorkerReportHandler
import nsu.crackhash.manager.api.CrackHashWorkerReportRequest
import nsu.crackhash.manager.config.KafkaTopicsConfiguration
import nsu.crackhash.manager.infra.Log
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaTopicsListener(
    private val crackHashWorkerReportHandler: CrackHashWorkerReportHandler,
    private val objectMapper: ObjectMapper
) {

    @Log
    @KafkaListener(topics = [KafkaTopicsConfiguration.REPORTS_TOPIC_NAME])
    fun listenReports(data: String) {
        crackHashWorkerReportHandler.crackHashWorkerReportHandle(
            objectMapper.readValue(
                data,
                CrackHashWorkerReportRequest::class.java
            )
        )
    }
}