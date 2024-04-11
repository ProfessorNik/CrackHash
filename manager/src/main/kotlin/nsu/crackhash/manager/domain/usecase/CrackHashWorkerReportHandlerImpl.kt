package nsu.crackhash.manager.domain.usecase

import nsu.crackhash.manager.api.CrackHashWorkerReportHandler
import nsu.crackhash.manager.api.CrackHashWorkerReportRequest
import nsu.crackhash.manager.domain.model.RequestId
import nsu.crackhash.manager.domain.port.ManagerCrackHashInfoRepository
import org.springframework.stereotype.Service

@Service
class CrackHashWorkerReportHandlerImpl(private val managerCrackHashInfoRepository: ManagerCrackHashInfoRepository) :
    CrackHashWorkerReportHandler {

    override fun crackHashWorkerReportHandle(crackHashWorkerReportRequest: CrackHashWorkerReportRequest) {
        val requestId = RequestId(crackHashWorkerReportRequest.requestId)
        val managerCrackHashInfo = managerCrackHashInfoRepository.findById(requestId) ?: throw NoSuchElementException()
        val managerCrackHashInfoWithWorkerResponse =
            managerCrackHashInfo.withWorkerResponse(crackHashWorkerReportRequest)
        managerCrackHashInfoRepository.add(managerCrackHashInfoWithWorkerResponse)
    }
}