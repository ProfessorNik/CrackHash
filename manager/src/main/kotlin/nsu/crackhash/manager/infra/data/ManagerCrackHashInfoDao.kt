package nsu.crackhash.manager.infra.data

import nsu.crackhash.manager.domain.model.ManagerCrackHashInfo
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface ManagerCrackHashInfoDao : CrudRepository<ManagerCrackHashInfo, UUID>