package nsu.crackhash.worker.infra

import nsu.crackhash.worker.domain.usecase.ManagerGetaway
import nsu.crackhash.worker.api.ReportAboutCracking
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class ManagerRestApiClient(private val configuration: RestApiManagerConfiguration) : ManagerGetaway {

    val managerRestClient = RestClient.builder().baseUrl(configuration.managerUrl).build()

    companion object {
        const val MANAGER_HASH_CRACK_REQUEST = "/internal/api/manager/hash/crack/request"
    }

    override fun reportAboutCracking(reportAboutCracking: ReportAboutCracking) {
        managerRestClient.post()
            .uri(MANAGER_HASH_CRACK_REQUEST)
            .body(reportAboutCracking)
            .exchange { _, res -> if (!res.statusCode.is2xxSuccessful) {
                throw Exception("Request to manager with url=${configuration.managerUrl} failed")
            } }
    }
}