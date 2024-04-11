package nsu.crackhash.manager.api

interface CrackHash {

    fun crackHash(request: CrackHashRequest): CrackHashResponse
}