package nsu.crackhash.e2e

import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.with

@TestConfiguration(proxyBeanMethods = false)
class TestE2eApplication

fun main(args: Array<String>) {
    fromApplication<E2eApplication>().with(TestE2eApplication::class).run(*args)
}
