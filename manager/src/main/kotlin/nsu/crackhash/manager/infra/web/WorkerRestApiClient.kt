package nsu.crackhash.manager.infra.web

import nsu.crackhash.manager.api.WorkerGetaway
import nsu.crackhash.manager.config.RestConfigurationProperties
import nsu.crackhash.manager.domain.model.WorkerId
import nsu.crackhash.manager.domain.model.WorkerInfo
import nsu.crackhash.manager.domain.model.WorkerTask
import nsu.crackhash.manager.infra.data.WorkerTaskAssignRequest
import org.springframework.web.client.RestClient
import java.util.*

class WorkerRestApiClient(restConfigurationProperties: RestConfigurationProperties) : WorkerGetaway {

    companion object {
        private const val WORKER_HASH_CRACK_TASK_URL = "/internal/api/worker/hash/crack/task"
    }

    private val workers: List<Worker> = restConfigurationProperties.workers.map { baseUrl ->
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
            val worker = workers.find { it.workerId == task.workerId }
                ?: throw NoSuchElementException("Worker with id ${task.workerId} not found")

            sendTask(worker, task)
        }
    }

    private fun sendTask(worker: Worker, task: WorkerTask) {
        worker.restClient
            .post()
            .uri(WORKER_HASH_CRACK_TASK_URL)
            .body(WorkerTaskAssignRequest.emerge(task))
            .exchange { _, res ->
                if (!res.statusCode.is2xxSuccessful) {
                    throw Exception("Request to worker with id=${worker.workerId} url=${worker.baseUrl} failed")
                }
            }
    }
}

data class Worker(
    val workerId: WorkerId,
    val baseUrl: String,
    val restClient: RestClient
)
