package nsu.crackhash.worker.domain.usecase

import nsu.crackhash.worker.api.ReportAboutCracking

interface ManagerGetaway {

    fun reportAboutCracking(reportAboutCracking: ReportAboutCracking)
}