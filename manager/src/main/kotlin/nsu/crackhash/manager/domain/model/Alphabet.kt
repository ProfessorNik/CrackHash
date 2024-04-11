package nsu.crackhash.manager.domain.model

@JvmInline
value class Symbol(val value: String) {
    init {
        require(value.length == 1) {
            "Symbol length must be 1, but length=${value.length}"
        }
    }
}

data class Alphabet(
    val symbols: List<Symbol>
) {
    companion object {
        fun emerge(): Alphabet {
            val symbolAlphabet = (97..122).map { it.toChar().toString() }
            val digitAlphabet = (0..9).map { it.toString() }.toList()
            val alphabet = (symbolAlphabet + digitAlphabet).map { Symbol(it) }

            return Alphabet(alphabet)
        }
    }
}
