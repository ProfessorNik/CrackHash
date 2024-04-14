package nsu.crackhash.e2e.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.core.io.ResourceLoader
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.ComposeContainer

@TestConfiguration
class MicroservicesTestConfiguration {

    companion object {
        private const val DOCKER_COMPOSE_PATH = "classpath:docker-compose.yml"
        private const val MANAGER_SERVICE_NAME = "manager"
    }


    @Bean
    @ServiceConnection
    fun microservices(resourceLoader: ResourceLoader): ComposeContainer {
        val resource = resourceLoader.getResource(DOCKER_COMPOSE_PATH)
        return ComposeContainer(resource.file)
            .withExposedService(MANAGER_SERVICE_NAME, 8080)
            .withLocalCompose(true)
    }

    @Bean
    @Primary
    fun managerTestClient(microservices: ComposeContainer): WebTestClient {
        val managerPort = microservices.getContainerByServiceName(MANAGER_SERVICE_NAME).orElseThrow().firstMappedPort
        return WebTestClient.bindToServer()
            .baseUrl("http://localhost:${managerPort}")
            .build()
    }
}

