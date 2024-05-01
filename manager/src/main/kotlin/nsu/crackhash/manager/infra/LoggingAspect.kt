package nsu.crackhash.manager.infra

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class LoggingAspect {

    private val log = LoggerFactory.getLogger(LoggingAspect::class.java)

    @Pointcut("@annotation(Log)")
    fun logPointcut() {
    }

    @AfterReturning(value = "logPointcut()", returning = "returnValue")
    fun loggingAfterReturning(jp: JoinPoint, returnValue: Any?) {
        log.info(
            """
                Before: 
                class=${jp.`this`.javaClass}
                method=${jp.signature.name}
                args=${jp.args.map { it.toString() }}     
                return=$returnValue 
            """.trimIndent()
        )
    }

    @AfterThrowing(value = "logPointcut()", throwing = "throwing")
    fun loggingAfterThrowing(jp: JoinPoint, throwing: Throwable?) {
        log.info(
            """
                Before: 
                class=${jp.`this`.javaClass}
                method=${jp.signature.name}
                args=${jp.args.map { it.toString() }}     
                throwing=$throwing
            """.trimIndent()
        )
    }
}