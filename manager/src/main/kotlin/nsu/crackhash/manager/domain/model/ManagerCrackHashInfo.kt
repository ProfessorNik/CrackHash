package nsu.crackhash.manager.domain.model

import nsu.crackhash.manager.api.CrackHashRequest
import nsu.crackhash.manager.api.CrackHashStatus
import nsu.crackhash.manager.api.CrackHashWorkerReportRequest
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import kotlin.time.Duration


typealias CrackedWord = String
typealias WordMaxLength = Int
typealias PartNumber = Int
typealias Hash = String

data class WorkerTaskInfo(
    val workerId: WorkerId,
    val partNumber: PartNumber,
    val status: CrackHashStatus,
    val timeLeftToComplete: Duration
)

@Document
data class ManagerCrackHashInfo(
    @Id
    val id: UUID,
    val crackHashStatus: CrackHashStatus,
    val hash: Hash,
    val maxLength: WordMaxLength,
    val data: Set<CrackedWord>,
    val workInfo: List<WorkerTaskInfo>
) {

    val requestId: RequestId
        get() = RequestId(id)

    init {
        require(maxLength > 0) {
            "Max length must be more than zero, but maxLength=$maxLength"
        }
        require(!data.any { it.isBlank() || it.length > maxLength }) {
            "Cracked word length must be between 0 and $maxLength"
        }
        require(hash.isNotBlank()) {
            "Hash must not be blank"
        }
        require(workInfo.isNotEmpty()) {
            "There must be at least one worker"
        }
        require(workInfo.any { it.partNumber >= 0 && it.partNumber < workInfo.size }) {
            "Part number must be less than workInfo.size=${workInfo.size}"
        }
    }

    companion object {
        private val alphabet: Alphabet = Alphabet.emerge()

        fun emerge(requestId: UUID, request: CrackHashRequest, workersInfo: List<WorkerInfo>): ManagerCrackHashInfo {
            return ManagerCrackHashInfo(
                requestId,
                CrackHashStatus.IN_PROGRESS,
                request.hash,
                request.maxLength,
                setOf(),
                workersInfo.mapIndexed { index, it ->
                    WorkerTaskInfo(
                        workerId = it.workerId,
                        partNumber = index,
                        status = CrackHashStatus.IN_PROGRESS,
                        timeLeftToComplete = Duration.INFINITE
                    )
                }
            )
        }
    }

    fun happenedError(): ManagerCrackHashInfo {
        return copy(crackHashStatus = CrackHashStatus.ERROR)
    }

    fun generateTasks(): List<WorkerTask> {
        val taskCount = workInfo.size
        return workInfo.map { worker ->
            WorkerTask(
                worker.workerId.value,
                requestId,
                hash,
                maxLength,
                worker.partNumber,
                taskCount,
                alphabet
            )
        }
    }

    fun withWorkerResponse(crackHashWorkerReportRequest: CrackHashWorkerReportRequest): ManagerCrackHashInfo {
        val updatedWorkInfo = workInfo.map {
            if (it.partNumber == crackHashWorkerReportRequest.partNumber && it.status != CrackHashStatus.READY) {
                if (crackHashWorkerReportRequest.isReady) {
                    it.copy(
                        status = CrackHashStatus.READY,
                        timeLeftToComplete = crackHashWorkerReportRequest.timeLeftToComplete
                    )
                } else {
                    it.copy(timeLeftToComplete = crackHashWorkerReportRequest.timeLeftToComplete)
                }
            } else {
                it
            }
        }

        val answers = crackHashWorkerReportRequest.answers

        return if (updatedWorkInfo.any { it.status == CrackHashStatus.READY }) {
            copy(crackHashStatus = CrackHashStatus.READY, workInfo = updatedWorkInfo, data = data + answers.words)
        } else {
            copy(workInfo = updatedWorkInfo, data = data + answers.words)
        }
    }
}

