package nsu.crackhash.worker.domain.usecase

import nsu.crackhash.worker.api.Answers
import nsu.crackhash.worker.api.CrackHash
import nsu.crackhash.worker.api.CrackHashRequest
import nsu.crackhash.worker.api.ReportAboutCracking
import org.apache.commons.codec.digest.DigestUtils
import org.paukov.combinatorics3.Generator
import org.springframework.stereotype.Service
import java.math.BigInteger
import java.util.stream.Stream
import kotlin.concurrent.thread
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Service
class CrackHashImpl(val managerGetaway: ManagerGetaway) : CrackHash {

    override fun crackHash(request: CrackHashRequest) {
        val counter = Counter(
            quantityPermutationWithRepetitionByMaxLengthByOneWorker(
                request.alphabet.symbols,
                request.maxLength,
                request.partCount
            )
        )

        val timeLeftToCompletePublisherThread = thread {
            try {
                while (!Thread.interrupted()) {
                    Thread.sleep(1000)
                    val timeLeftToComplete = counter.timeLeftToComplete()
                    println("COUNTER: $counter")
                    managerGetaway.reportAboutCracking(
                        ReportAboutCracking(
                            request.requestId,
                            request.partNumber,
                            Answers(listOf()),
                            isReady = false,
                            timeLeftToComplete
                        )
                    )
                }
            } catch (interruptedException: InterruptedException) {
                return@thread
            }
        }

        val result = buildList {
            repeat(request.maxLength) {
                addAll(
                    buildHashCracker(
                        buildCrackHashSource(
                            request,
                            wordLength = it + 1,
                            sideEffectOnEachWord = { counter.increment() }
                        )
                    ).toList()
                )
            }
        }

        timeLeftToCompletePublisherThread.interrupt()
        timeLeftToCompletePublisherThread.join()
        println("COUNTER after end cracking: $counter")

        managerGetaway.reportAboutCracking(
            ReportAboutCracking(
                request.requestId,
                request.partNumber,
                Answers(result),
                isReady = true,
                timeLeftToComplete = 0.milliseconds
            )
        )
    }

    class Counter(private val total: Long) {
        private var count = 0L
        private val startTime: Long = System.currentTimeMillis()

        fun increment() {
            count++
        }

        fun timeLeftToComplete(): Duration {
            val incrementsRemaining = total - count
            val millisecondsRemaining = incrementsRemaining / incrementsPerMillis()
            return millisecondsRemaining.milliseconds
        }

        private fun incrementsPerMillis(): Double {
            val currentTime = System.currentTimeMillis()
            val elapsedTimeInMillis = currentTime - startTime
            return count.toDouble() / elapsedTimeInMillis
        }

        override fun toString(): String {
            return "Counter(total=$total, count=$count, startTime=$startTime, timeLeftToComplete=${timeLeftToComplete()})"
        }
    }

    data class CrackHashSource(
        val alphabet: List<String>,
        val wordLength: Int,
        val partNumber: Int,
        val hash: String,
        val partCount: Int,
        val sideEffectOnEachWord: () -> Unit
    )

    private fun buildCrackHashSource(
        request: CrackHashRequest,
        wordLength: Int,
        sideEffectOnEachWord: () -> Unit
    ): CrackHashSource {
        return CrackHashSource(
            request.alphabet.symbols,
            wordLength,
            request.partNumber,
            request.hash,
            request.partCount,
            sideEffectOnEachWord
        )
    }

    private fun buildHashCracker(source: CrackHashSource): Stream<String> {
        return Generator.permutation(source.alphabet)
            .withRepetitions(source.wordLength)
            .stream()
            .skip(quantitySkipPermutationWithRepetition(source))
            .limit(quantityLimitPermutationWithRepetition(source))
            .map { it.joinToString(separator = "") }
            .peek { source.sideEffectOnEachWord() }
            .filter { word -> hashMatches(word, source.hash) }

    }

    private fun hashMatches(word: String, requestHash: String): Boolean {
        return DigestUtils.md5Hex(word) == requestHash
    }

    private fun quantitySkipPermutationWithRepetition(source: CrackHashSource): Long {
        return quantityPermutationWithRepetitionByOneWorker(source) * source.partNumber
    }

    private fun quantityLimitPermutationWithRepetition(source: CrackHashSource): Long {
        return quantityPermutationWithRepetitionByOneWorker(source)
    }

    private fun quantityPermutationWithRepetitionByOneWorker(source: CrackHashSource): Long {
        return quantityPermutationWithRepetition(source.alphabet, source.wordLength) / source.partCount
    }

    private fun quantityPermutationWithRepetition(alphabet: List<*>, wordLength: Int): Long {
        return BigInteger.valueOf(alphabet.size.toLong()).pow(wordLength).toLong()
    }

    private fun quantityPermutationWithRepetitionByMaxLengthByOneWorker(
        alphabet: List<*>,
        maxLength: Int,
        partCount: Int
    ): Long {
        return (1..maxLength).sumOf { quantityPermutationWithRepetition(alphabet, it) / partCount }
    }


}