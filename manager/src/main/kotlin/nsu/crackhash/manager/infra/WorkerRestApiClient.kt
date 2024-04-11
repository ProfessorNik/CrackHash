package nsu.crackhash.manager.infra

import nsu.crackhash.manager.config.WorkersConfiguration
import nsu.crackhash.manager.domain.model.WorkerId
import nsu.crackhash.manager.domain.model.WorkerInfo
import nsu.crackhash.manager.domain.model.WorkerTask
import nsu.crackhash.manager.domain.port.WorkerGetaway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.util.*

data class Worker(
    val workerId: WorkerId,
    val baseUrl: String,
    val restClient: RestClient
)

data class WorkerTaskAssignRequest(
    val requestId: UUID,
    val hash: String,
    val maxLength: Int,
    val partNumber: Int,
    val partCount: Int,
    val alphabet: AlphabetDto
) {

    companion object {

        fun emerge(task: WorkerTask): WorkerTaskAssignRequest {
            return WorkerTaskAssignRequest(
                task.requestId.value,
                task.hash,
                task.maxLength,
                task.partNumber,
                task.partCount,
                AlphabetDto(task.alphabet.symbols.map { it.value })
            )
        }
    }
}

data class AlphabetDto(
    val symbols: List<String>
)

@Component
class WorkerRestApiClient @Autowired constructor(workersConfiguration: WorkersConfiguration) : WorkerGetaway {

    companion object {
        private const val WORKER_HASH_CRACK_TASK = "/internal/api/worker/hash/crack/task"
    }

    val workers = workersConfiguration.workers.map { baseUrl ->
        Worker(
            workerId = WorkerId(UUID.randomUUID()),
            baseUrl,
            RestClient.builder().baseUrl(baseUrl).build()
        )
    }

    override fun workersInfo(): List<WorkerInfo> {
        return workers.map { WorkerInfo(it.workerId) }
    }

    override fun assignTasks(tasks: List<WorkerTask>) {
        tasks.forEach { task ->
            val worker = workers.find { it.workerId == task.workerId } ?: throw NoSuchElementException()

            worker.restClient
                .post()
                .uri(WORKER_HASH_CRACK_TASK)
                .body(WorkerTaskAssignRequest.emerge(task))
                .exchange { _, res ->
                    if (!res.statusCode.is2xxSuccessful) {
                        throw Exception("Request to worker with id=${worker.workerId} url=${worker.baseUrl} failed")
                    }
                }
        }
    }
}