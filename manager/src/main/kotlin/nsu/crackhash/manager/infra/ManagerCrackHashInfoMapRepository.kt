package nsu.crackhash.manager.infra

import nsu.crackhash.manager.domain.model.ManagerCrackHashInfo
import nsu.crackhash.manager.domain.model.RequestId
import nsu.crackhash.manager.domain.port.ManagerCrackHashInfoRepository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class ManagerCrackHashInfoMapRepository : ManagerCrackHashInfoRepository {

    private val dao: ConcurrentMap<RequestId, ManagerCrackHashInfo> = ConcurrentHashMap()

    override fun add(managerCrackHashInfo: ManagerCrackHashInfo) {
        dao[managerCrackHashInfo.requestId] = managerCrackHashInfo
    }

    override fun findById(requestId: RequestId): ManagerCrackHashInfo? {
        return dao[requestId]
    }
}