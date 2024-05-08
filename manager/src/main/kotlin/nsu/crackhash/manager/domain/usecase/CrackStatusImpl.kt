package nsu.crackhash.manager.domain.usecase

import nsu.crackhash.manager.api.CrackStatus
import nsu.crackhash.manager.api.CrackStatusRequest
import nsu.crackhash.manager.api.CrackStatusResponse
import nsu.crackhash.manager.api.ManagerCrackHashInfoRepository
import nsu.crackhash.manager.domain.model.RequestId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.time.Duration

@Service
class CrackStatusImpl @Autowired constructor(
    private val managerCrackHashInfoRepository: ManagerCrackHashInfoRepository
) : CrackStatus {

    override fun crackStatus(request: CrackStatusRequest): CrackStatusResponse {
        val managerCrackHashInfo = managerCrackHashInfoRepository.findById(RequestId(request.requestId))
            ?: throw NoSuchElementException()

        System.err.println(managerCrackHashInfo)

        return CrackStatusResponse(
            managerCrackHashInfo.crackHashStatus,
            managerCrackHashInfo.data.toList(),
            managerCrackHashInfo.workInfo.fold(Duration.ZERO) { acc, workerTaskInfo ->
                maxOf(acc, workerTaskInfo.timeLeftToComplete)
            }.toString()
        )
    }
}