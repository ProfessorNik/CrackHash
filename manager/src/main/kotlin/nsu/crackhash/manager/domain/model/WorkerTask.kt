package nsu.crackhash.manager.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document
data class WorkerTask(
    @Id
    val id: UUID,
    val requestId: RequestId,
    val hash: Hash,
    val maxLength: WordMaxLength,
    val partNumber: Int,
    val partCount: Int,
    val alphabet: Alphabet
) {

    val workerId: WorkerId
        get() = WorkerId(id)
}
