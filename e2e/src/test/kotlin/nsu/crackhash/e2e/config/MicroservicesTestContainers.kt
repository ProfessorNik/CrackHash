package nsu.crackhash.e2e.config

import org.testcontainers.containers.DockerComposeContainer
import java.io.File

class MicroservicesTestContainers {

    private val DOCKER_COMPOSE_PATH = "classpath:docker-compose.yml"

    private val MICROSERVICES_CONTAINERS = DockerComposeContainer(File(DOCKER_COMPOSE_PATH))
}
