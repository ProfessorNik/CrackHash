package nsu.crackhash.manager.domain.usecase

import nsu.crackhash.manager.api.*
import nsu.crackhash.manager.config.ApplicationConfigurationProperties
import nsu.crackhash.manager.domain.model.ManagerCrackHashInfo
import nsu.crackhash.manager.domain.model.RequestId
import nsu.crackhash.manager.api.ManagerCrackHashInfoRepository
import nsu.crackhash.manager.api.WorkerGetaway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class CrackHashImpl @Autowired constructor(
    private val workerGetaway: WorkerGetaway,
    private val managerCrackHashInfoRepository: ManagerCrackHashInfoRepository,
    private val scheduler: TaskScheduler,
    applicationConfigurationProperties: ApplicationConfigurationProperties
) : CrackHash {

    private val ttlInMillis = applicationConfigurationProperties.ttl

    override fun crackHash(request: CrackHashRequest): CrackHashResponse {
        val workersInfo = workerGetaway.workersInfo()
        val requestId = UUID.randomUUID()

        val managerCrackHashInfo = ManagerCrackHashInfo.emerge(requestId, request, workersInfo)
        managerCrackHashInfoRepository.add(managerCrackHashInfo)

        val tasks = managerCrackHashInfo.generateTasks()
        runCatching { workerGetaway.assignTasks(tasks) }
            .onFailure {
                val managerCrackHashInfoCrashed = managerCrackHashInfo.happenedError()
                managerCrackHashInfoRepository.add(managerCrackHashInfoCrashed)
            }

        scheduler.schedule(
            { happenedErrorIfNotCompleted(managerCrackHashInfo.requestId) },
            Instant.now().plusMillis(ttlInMillis)
        )

        return CrackHashResponse(managerCrackHashInfo.requestId.value)
    }

    fun happenedErrorIfNotCompleted(requestId: RequestId) {
        managerCrackHashInfoRepository.findById(requestId)?.let {
            if (it.crackHashStatus == CrackHashStatus.IN_PROGRESS) {
                val managerCrackHashInfoCrashed = it.happenedError()
                managerCrackHashInfoRepository.add(managerCrackHashInfoCrashed)
            }
        }
    }
}