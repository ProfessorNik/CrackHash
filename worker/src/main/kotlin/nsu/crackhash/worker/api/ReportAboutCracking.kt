package nsu.crackhash.worker.api

import java.util.UUID
import kotlin.time.Duration

data class ReportAboutCracking(
    val requestId: UUID,
    val partNumber: Int,
    val answers: Answers,
    val isReady: Boolean,
    val timeLeftToComplete: Duration
)

data class Answers(
    val words: List<String>
)
