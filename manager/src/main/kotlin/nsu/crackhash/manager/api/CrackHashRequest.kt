package nsu.crackhash.manager.api

data class CrackHashRequest(
    val hash: String,
    val maxLength: Int
)
