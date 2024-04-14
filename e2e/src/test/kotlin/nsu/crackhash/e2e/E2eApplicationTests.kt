package nsu.crackhash.e2e

import nsu.crackhash.e2e.config.MicroservicesTestConfiguration
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriBuilder
import org.testcontainers.shaded.org.awaitility.Awaitility
import java.net.URI
import java.time.Duration
import java.util.*

@SpringBootTest
@Import(MicroservicesTestConfiguration::class)
class E2eApplicationTests {

    @Autowired
    lateinit var managerTestClient: WebTestClient

    @Test
    fun contextLoads() {
    }

    @Test
    fun baseTest() {
        val word = "abcd"
        val wordLength = 4
        val hashWord = "e2fc714c4727ee9395f324cd2e7f331f"

        val crackHashResponse = managerTestClient.post()
            .uri(CRACK_HASH_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(CrackHashRequest(hashWord, wordLength))
            .exchange()
            .expectStatus().isOk
            .expectBody(CrackHashResponse::class.java)
            .returnResult()
            .responseBody


        Assertions.assertNotNull(crackHashResponse)
        Awaitility.await()
            .timeout(Duration.ofSeconds(5))


        managerTestClient.get()
            .uri(crackStatusURI(crackHashResponse!!.requestId))
            .exchange()
            .expectStatus().isOk
            .expectBody(CrackStatusResponse::class.java)
            .value { response ->
                Assertions.assertEquals(CrackHashStatus.READY, response.status)
                Assertions.assertTrue(response.data?.contains(word) ?: false)
            }
    }

}

private const val CRACK_HASH_URL = "/api/hash/crack"
private const val CRACK_STATUS_URL = "/api/hash/status"

val crackStatusURI: (requestId: UUID) -> ((uriBuilder: UriBuilder) -> URI) =
    { requestId ->
        { uriBuilder ->
            uriBuilder.path(CRACK_STATUS_URL)
                .queryParam("requestId", requestId)
                .build()
        }
    }

data class CrackHashRequest(val hash: String, val maxLength: Int)
data class CrackHashResponse(val requestId: UUID)
data class CrackStatusResponse(val status: CrackHashStatus, val data: List<String>?)
enum class CrackHashStatus {
    IN_PROGRESS,
    READY,
    ERROR
}


