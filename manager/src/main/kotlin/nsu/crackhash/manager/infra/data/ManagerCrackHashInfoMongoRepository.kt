package nsu.crackhash.manager.infra.data

import nsu.crackhash.manager.domain.model.ManagerCrackHashInfo
import nsu.crackhash.manager.domain.model.RequestId
import nsu.crackhash.manager.api.ManagerCrackHashInfoRepository

class ManagerCrackHashInfoMongoRepository(
    private val managerCrackHashInfoMongoDao: ManagerCrackHashInfoDao
) : ManagerCrackHashInfoRepository {
    override fun add(managerCrackHashInfo: ManagerCrackHashInfo) {
        System.err.println("Сохранить монго")
        managerCrackHashInfoMongoDao.save(managerCrackHashInfo)
    }

    override fun findById(requestId: RequestId): ManagerCrackHashInfo? {
        System.err.println("Поиск по id у монго")
        return managerCrackHashInfoMongoDao.findById(requestId.value).orElse(null)
    }
}