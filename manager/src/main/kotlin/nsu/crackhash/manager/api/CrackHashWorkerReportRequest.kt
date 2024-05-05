package nsu.crackhash.manager.api

import java.util.*
import kotlin.time.Duration

data class CrackHashWorkerReportRequest(
    val requestId: UUID,
    val partNumber: Int,
    val answers: Answers,
    val isReady: Boolean,
    val timeLeftToComplete: Duration
)

data class Answers(
    val words: List<String>
)
