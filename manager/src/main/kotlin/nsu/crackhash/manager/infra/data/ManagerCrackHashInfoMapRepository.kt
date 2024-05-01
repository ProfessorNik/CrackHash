package nsu.crackhash.manager.infra.data

import nsu.crackhash.manager.domain.model.ManagerCrackHashInfo
import nsu.crackhash.manager.domain.model.RequestId
import nsu.crackhash.manager.api.ManagerCrackHashInfoRepository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class ManagerCrackHashInfoMapRepository : ManagerCrackHashInfoRepository {

    private val dao: ConcurrentMap<RequestId, ManagerCrackHashInfo> = ConcurrentHashMap()

    override fun add(managerCrackHashInfo: ManagerCrackHashInfo) {
        System.err.println("Сохранить Map")
        dao[managerCrackHashInfo.requestId] = managerCrackHashInfo
    }

    override fun findById(requestId: RequestId): ManagerCrackHashInfo? {
        System.err.println("Поиск по id у Map")
        return dao[requestId]
    }
}