package org.example.io.github.daniils_loputevs.rsql_runtime_executor.selector

class SelectorParseIterator(val selector: String, var position: Int = 0) : CharIterator() {
    val curr: Char get() = selector[position]

    override fun nextChar(): Char = selector[position++]
    override fun hasNext(): Boolean = position < selector.length


    fun skipWhitespace() = kotlin.run {
        while (position < selector.length && selector[position].isWhitespace()) position++
    }

    override fun toString(): String = "ParseIterator(curr='${currToStr()}', pos=$position, selector='$selector')"

    private fun currToStr() = try {
        curr.toString()
    } catch (e: Exception) {
        "~~Exception on call \$curr~~"
    }

}

/* === PREDICATES === */

fun SelectorParseIterator.isThis(): Boolean = selector.startsWith("this", position)
fun SelectorParseIterator.isNull(): Boolean = selector.startsWith("null", position)
fun SelectorParseIterator.isBoolean(): Boolean = when {
    selector.startsWith("true", position) -> true
    selector.startsWith("false", position) -> true
    else -> false
}

fun SelectorParseIterator.isString(): Boolean = selector[position].let { ch -> ch == '"' || ch == '\'' }


/* === PARSE === */


/** Mutate iterator state */
fun SelectorParseIterator.parseConstThis(): Token = run { this.position += 4; return TokenThis }

/** Mutate iterator state */
fun SelectorParseIterator.parseConstNull(): Token = run { this.position += 4; return TokenNull }

/** Mutate iterator state */
fun SelectorParseIterator.parseConstNumber(): Token {
    val start = position
    while (this.hasNext()) {
        val ch = curr
        if (ch.isDigit() || ch == '.') break
        position++
    }
    val number = selector.substring(start, position)
    return if (number.contains('.')) TokenFloat(number.toBigDecimal()) else TokenInt(number.toBigInteger())
}

/** Mutate iterator state */
fun SelectorParseIterator.parseConstBoolean(): Token = TokenBoolean(
    when {
        selector.startsWith("true", position) -> true
        selector.startsWith("false", position) -> true
        else -> throw RuntimeException("unreachable")
    }
)

/** Mutate iterator state */
fun SelectorParseIterator.parseConstString(): Token {
    val start = ++position // Skip opening quote
    while (this.hasNext()) {
        val ch = curr
        if (ch == '"' || ch == '\'') break
        position++
    }

    val value = selector.substring(start, position)
    return TokenString(value)
}

/** Mutate iterator state */
fun SelectorParseIterator.parseIdentifier(): Token {
    val start = position
    while (this.hasNext()) {
        val ch = curr
        if (ch == '.' || ch == '[' || ch == ']') break
        position++
    }

    return TokenIdentifier(
        selector.substring(start, position)
            .also { position-- /* reverse pointer to last char, one of then : '.' || '[' || ']' */ }
    )
}
