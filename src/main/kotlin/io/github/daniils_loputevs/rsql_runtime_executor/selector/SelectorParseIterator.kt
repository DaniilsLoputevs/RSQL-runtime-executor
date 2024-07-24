package org.example.io.github.daniils_loputevs.rsql_runtime_executor.selector

class SelectorParseIterator(val selector: String, var position: Int = 0) : CharIterator() {
    val currChar: Char get() = selector[position]

    override fun nextChar(): Char = selector[position++]
    override fun hasNext(): Boolean = position < selector.length


    fun skipWhitespace() = kotlin.run {
        while (position < selector.length && selector[position].isWhitespace()) position++
    }

    override fun toString(): String = "ParseIterator(${stateToString()}"

    fun stateToString() = "curr='${currToStr()}', pos=$position, selector='$selector'"

    private fun currToStr() = try {
        if (position < selector.length) currChar.toString()
        else "~~End of Selector~~"
    } catch (e: Exception) {
        "~~Exception on call \$currChar~~"
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
fun SelectorParseIterator.parseConstThis(): TokenThis = run { this.position += 3; return TokenThis }

/** Mutate iterator state */
fun SelectorParseIterator.parseConstNull(): TokenNull = run { this.position += 3; return TokenNull }

/** Mutate iterator state */
fun SelectorParseIterator.parseConstNumber(): AnyToken {
    val start = position
    while (this.hasNext()) {
        val ch = currChar
        if (ch.isDigit() || ch == '.')
            position++
        else break
    }
    if (start == position) throw SelectorParseException("expected number constant but find=''")
    val number = selector.substring(start, position)
    position-- // set position at last char after iteration because next char is other token type
    return if (number.contains('.')) TokenFloat(number.toBigDecimal()) else TokenInt(number.toBigInteger())
}

/** Mutate iterator state */
fun SelectorParseIterator.parseConstBoolean(): TokenBoolean = TokenBoolean(
    when {
        selector.startsWith("true", position) -> true.also { position += 3 }
        selector.startsWith("false", position) -> true.also { position += 4 }
        else -> throw SelectorParseException("Expected boolean but find='${selector.substring(position)}'")
    }
)

/** Mutate iterator state */
fun SelectorParseIterator.parseConstString(): TokenString {
    val start = ++position // Skip opening quote
    while (this.hasNext()) {
        val ch = currChar
        if (ch == '"' || ch == '\'') break
        position++
    }

    val value = selector.substring(start, position)
    return TokenString(value)
}

/** Mutate iterator state */
fun SelectorParseIterator.parseIdentifier(): TokenIdentifier {
    val start = position
    while (this.hasNext()) {
        val ch = currChar
        if (ch == '.' || ch == '[' || ch == ']') break
        position++
    }

    return TokenIdentifier(
        selector.substring(start, position)
            .also { position-- /* reverse pointer to last char, one of then : '.' || '[' || ']' */ }
    )
}
