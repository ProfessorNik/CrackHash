package nsu.crackhash.worker.infra

import nsu.crackhash.worker.api.ClientUrl
import nsu.crackhash.worker.api.CrackHash
import nsu.crackhash.worker.api.CrackHashRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture.runAsync

@RestController
class ClientCrackHashController(val crackHash: CrackHash) {

    @PostMapping(ClientUrl.WORKER_HASH_CRACK_TASK)
    fun crackHash(@RequestBody crackHashRequest: CrackHashRequest) {
        runAsync { crackHash.crackHash(crackHashRequest) }
    }
}