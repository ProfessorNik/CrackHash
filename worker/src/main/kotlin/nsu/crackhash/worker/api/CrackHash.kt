package nsu.crackhash.worker.api

import org.springframework.scheduling.annotation.Async

interface CrackHash {

    @Async
    fun crackHash(request: CrackHashRequest)
}