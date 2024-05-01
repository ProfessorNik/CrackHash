package nsu.crackhash.manager.api

import nsu.crackhash.manager.domain.model.ManagerCrackHashInfo
import nsu.crackhash.manager.domain.model.RequestId

interface ManagerCrackHashInfoRepository {

    fun add(managerCrackHashInfo: ManagerCrackHashInfo)

    fun findById(requestId: RequestId): ManagerCrackHashInfo?
}