package nsu.crackhash.manager.infra.data

import nsu.crackhash.manager.domain.model.WorkerTask
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class WorkerTaskAssignRequest(
    @Id
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
