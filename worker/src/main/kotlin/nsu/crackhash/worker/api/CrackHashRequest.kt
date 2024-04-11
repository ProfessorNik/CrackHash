package nsu.crackhash.worker.api

import java.util.*

data class CrackHashRequest(
    val requestId: UUID,
    val hash: String,
    val maxLength: Int,
    val partNumber: Int,
    val partCount: Int,
    val alphabet: AlphabetDto
)

data class AlphabetDto(
    val symbols: List<String>
)

