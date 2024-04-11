package nsu.crackhash.worker.domain.usecase

import nsu.crackhash.worker.api.Answers
import nsu.crackhash.worker.api.CrackHash
import nsu.crackhash.worker.api.CrackHashRequest
import nsu.crackhash.worker.api.ReportAboutCracking
import org.apache.commons.codec.digest.DigestUtils
import org.paukov.combinatorics3.Generator
import org.springframework.stereotype.Service
import java.math.BigInteger

@Service
class CrackHashImpl(val managerGetaway: ManagerGetaway) : CrackHash {

    override fun crackHash(request: CrackHashRequest) {
        val hashMatchesWords = Generator.permutation(request.alphabet.symbols)
            .withRepetitions(request.maxLength)
            .stream()
            .skip(quantitySkipPermutationWithRepetition(request))
            .limit(quantityLimitPermutationWithRepetition(request))
            .map { it.joinToString(separator = "") }
            .filter { word -> hashMatches(word, request.hash) }
            .toList().toList()

        return managerGetaway.reportAboutCracking(
            ReportAboutCracking(
                request.requestId,
                request.partNumber,
                Answers(
                    hashMatchesWords
                )
            )
        )
    }

    private fun hashMatches(word: String, requestHash: String): Boolean {
        return DigestUtils.md5Hex(word) == requestHash
    }

    private fun quantitySkipPermutationWithRepetition(request: CrackHashRequest): Long {
        return quantityPermutationWithRepetitionByOneWorker(request) * request.partNumber
    }

    private fun quantityLimitPermutationWithRepetition(request: CrackHashRequest): Long {
        return quantitySkipPermutationWithRepetition(request) + quantityPermutationWithRepetitionByOneWorker(request)
    }

    private fun quantityPermutationWithRepetitionByOneWorker(request: CrackHashRequest): Long {
        return quantityPermutationWithRepetition(request.alphabet.symbols, request.maxLength) / request.partCount
    }

    private fun quantityPermutationWithRepetition(alphabet: List<*>, maxLength: Int): Long {
        return BigInteger.valueOf(alphabet.size.toLong()).pow(maxLength).toLong()
    }
}