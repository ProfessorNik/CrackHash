package nsu.crackhash.manager.domain.model


data class WorkerTask(
    val workerId: WorkerId,
    val requestId: RequestId,
    val hash: Hash,
    val maxLength: WordMaxLength,
    val partNumber: Int,
    val partCount: Int,
    val alphabet: Alphabet
)
