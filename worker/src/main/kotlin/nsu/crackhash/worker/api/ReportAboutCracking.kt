package nsu.crackhash.worker.api

import java.util.UUID

data class ReportAboutCracking(
    val requestId: UUID,
    val partNumber: Int,
    val answers: Answers
)

data class Answers(
    val words: List<String>
)
