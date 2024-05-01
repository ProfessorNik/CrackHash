package nsu.crackhash.manager.infra.web

import com.fasterxml.jackson.databind.ObjectMapper
import nsu.crackhash.manager.config.KafkaTopicsConfiguration
import nsu.crackhash.manager.config.KafkaTopicsConfigurationProperties
import nsu.crackhash.manager.domain.model.WorkerId
import nsu.crackhash.manager.domain.model.WorkerInfo
import nsu.crackhash.manager.domain.model.WorkerTask
import nsu.crackhash.manager.api.WorkerGetaway
import nsu.crackhash.manager.infra.Log
import nsu.crackhash.manager.infra.data.WorkerTaskAssignRequest
import nsu.crackhash.manager.infra.data.WorkerTaskDao
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.annotation.Transactional
import java.util.NoSuchElementException
import java.util.UUID
import java.util.concurrent.CompletableFuture

open class WorkerKafkaClient(
    kafkaTopicsConfigurationProperties: KafkaTopicsConfigurationProperties,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val workerTaskDao: WorkerTaskDao,
    private val objectMapper: ObjectMapper
) : WorkerGetaway {

    private val workers: List<WorkerKafkaInfo> =
        List(kafkaTopicsConfigurationProperties.quantityWorkers) { workerIndex ->
            WorkerKafkaInfo(WorkerId(UUID.randomUUID()), partition = workerIndex)
        }

    override fun workersInfo(): List<WorkerInfo> = workers.map { WorkerInfo(it.workerId) }

    @Log
    @Transactional
    override fun assignTasks(tasks: List<WorkerTask>) {
        tasks.forEach { task ->
            sendTask(task)
                .whenComplete { _, throwable ->
                    if (throwable != null) {
                        workerTaskDao.save(task)
                        throw throwable
                    }
                }
        }
    }

    @Log
    @Scheduled(fixedDelay = 30000)
    @Transactional
    open fun assignTasksWhichDoesNotAssignedLater() {
        val tasks = workerTaskDao.findAll()
        tasks.forEach { task ->
            sendTask(task)
                .whenComplete { _, throwable ->
                    if (throwable == null) {
                        workerTaskDao.delete(task)
                    }
                }
        }
    }

    private fun sendTask(task: WorkerTask): CompletableFuture<SendResult<String, String>> {
        val worker = workers.find { it.workerId == task.workerId }
            ?: throw NoSuchElementException("Worker with id ${task.workerId} not found")


        return sendTask(worker, WorkerTaskAssignRequest.emerge(task))
    }

    private fun sendTask(
        worker: WorkerKafkaInfo,
        task: WorkerTaskAssignRequest
    ): CompletableFuture<SendResult<String, String>> {
        return kafkaTemplate.send(
            KafkaTopicsConfiguration.TASKS_TOPIC_NAME,
            worker.partition,
            worker.partition.toString(),
            objectMapper.writeValueAsString(task)
        )
    }
}

data class WorkerKafkaInfo(val workerId: WorkerId, val partition: Int)