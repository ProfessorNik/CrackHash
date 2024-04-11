package nsu.crackhash.manager.api


interface CrackStatus {

    fun crackStatus(request: CrackStatusRequest): CrackStatusResponse
}