package nsu.crackhash.worker.infra

import com.fasterxml.jackson.databind.ObjectMapper
import nsu.crackhash.worker.api.CrackHash
import nsu.crackhash.worker.api.CrackHashRequest
import nsu.crackhash.worker.api.KafkaTopic
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class KafkaTopicsListener(
    private val crackHash: CrackHash,
    private val objectMapper: ObjectMapper
) {

    @Log
    @KafkaListener(topics = [KafkaTopic.TASKS_TOPIC_NAME])
    fun listenTasks(data: String, acknowledgment: Acknowledgment) {
        crackHash.crackHash(
            objectMapper.readValue(
                data,
                CrackHashRequest::class.java
            )
        )

        acknowledgment.acknowledge()
    }
}