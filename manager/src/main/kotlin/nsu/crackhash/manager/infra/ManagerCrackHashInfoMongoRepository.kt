package nsu.crackhash.manager.infra

import nsu.crackhash.manager.domain.model.ManagerCrackHashInfo
import nsu.crackhash.manager.domain.model.RequestId
import nsu.crackhash.manager.domain.port.ManagerCrackHashInfoRepository

class ManagerCrackHashInfoMongoRepository(
    private val managerCrackHashInfoMongoDao: ManagerCrackHashInfoDao
) : ManagerCrackHashInfoRepository {
    override fun add(managerCrackHashInfo: ManagerCrackHashInfo) {
        managerCrackHashInfoMongoDao.save(managerCrackHashInfo)
    }

    override fun findById(requestId: RequestId): ManagerCrackHashInfo? {
        return managerCrackHashInfoMongoDao.findById(requestId.value).orElse(null)
    }
}