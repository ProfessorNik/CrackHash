package nsu.crackhash.manager.infra.web

import nsu.crackhash.manager.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class CrackHashController @Autowired constructor(
    val crackHash: CrackHash,
    val crackStatus: CrackStatus,
    val crackHashWorkerReportHandler: CrackHashWorkerReportHandler
) {

    @PostMapping(ManagerUrl.API_HASH_CRACK)
    fun crackHash(@RequestBody crackHashRequest: CrackHashRequest): CrackHashResponse {
        return crackHash.crackHash(crackHashRequest)
    }

    @GetMapping(ManagerUrl.API_HASH_STATUS)
    fun crackStatus(@RequestParam requestId: UUID): CrackStatusResponse {
        return crackStatus.crackStatus(CrackStatusRequest(requestId))
    }

    @PostMapping(ManagerUrl.INTERNAL_API_HASH_CRACK_REQUEST)
    fun crackHashWorker(@RequestBody crackHashWorkerReportRequest: CrackHashWorkerReportRequest) {
        crackHashWorkerReportHandler.crackHashWorkerReportHandle(crackHashWorkerReportRequest)
    }
}