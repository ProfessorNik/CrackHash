package nsu.crackhash.manager.api

import java.util.*

data class CrackHashWorkerReportRequest(
    val requestId: UUID,
    val partNumber: Int,
    val answers: Answers
)

data class Answers(
    val words: List<String>
)
