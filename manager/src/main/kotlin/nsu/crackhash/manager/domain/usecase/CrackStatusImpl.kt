package nsu.crackhash.manager.domain.usecase

import nsu.crackhash.manager.api.CrackStatus
import nsu.crackhash.manager.api.CrackStatusRequest
import nsu.crackhash.manager.api.CrackStatusResponse
import nsu.crackhash.manager.domain.model.RequestId
import nsu.crackhash.manager.domain.port.ManagerCrackHashInfoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CrackStatusImpl @Autowired constructor(
    private val managerCrackHashInfoRepository: ManagerCrackHashInfoRepository
) : CrackStatus {

    override fun crackStatus(request: CrackStatusRequest): CrackStatusResponse {
        val managerCrackHashInfo = managerCrackHashInfoRepository.findById(RequestId(request.requestId))
            ?: throw NoSuchElementException()

        return CrackStatusResponse(managerCrackHashInfo.crackHashStatus, managerCrackHashInfo.data)
    }
}