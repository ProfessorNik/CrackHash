package nsu.crackhash.manager.api

import nsu.crackhash.manager.domain.model.WorkerInfo
import nsu.crackhash.manager.domain.model.WorkerTask

interface WorkerGetaway {

    fun workersInfo(): List<WorkerInfo>

    fun assignTasks(tasks: List<WorkerTask>)
}