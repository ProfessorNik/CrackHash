package nsu.crackhash.manager.infra.data

import nsu.crackhash.manager.domain.model.WorkerTask
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface WorkerTaskDao : CrudRepository<WorkerTask, UUID>