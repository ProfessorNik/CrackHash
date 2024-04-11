package nsu.crackhash.manager.config

import nsu.crackhash.manager.infra.ManagerCrackHashInfoDao
import nsu.crackhash.manager.infra.ManagerCrackHashInfoMapRepository
import nsu.crackhash.manager.infra.ManagerCrackHashInfoMongoRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary


@Configuration
class ManagerConfig {

    @Bean
    fun managerCrackHashInfoMapRepository() =
        ManagerCrackHashInfoMapRepository()

    @Bean
    @Primary
    @ConditionalOnBean(ManagerCrackHashInfoDao::class)
    fun managerCrackHashInfoMongoRepository(managerCrackHashInfoDao: ManagerCrackHashInfoDao) =
        ManagerCrackHashInfoMongoRepository(managerCrackHashInfoDao)
}