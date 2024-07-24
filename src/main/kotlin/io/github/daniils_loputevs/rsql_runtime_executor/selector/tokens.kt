package org.example.io.github.daniils_loputevs.rsql_runtime_executor.selector

import java.math.BigDecimal
import java.math.BigInteger


sealed interface Token

open class Expression(val tokens: MutableList<Token> = mutableListOf()) : Token, Iterable<Token> {
    operator fun plusAssign(token: Token) = Unit.also { tokens.add(token) }

    override fun iterator(): Iterator<Token> = tokens.iterator()

    override fun toString(): String = "${this::class.simpleName}(tokens=$tokens)"
}

class IndexAccess(tokens: MutableList<Token> = mutableListOf()) : Expression(tokens) {
    override fun toString(): String = "${this::class.simpleName}(${super.tokens})"
}

data object TokenThis : Token {
    override fun toString(): String = "${this::class.simpleName}()"
}

data object TokenNull : Token {
    override fun toString(): String = "${this::class.simpleName}()"
}

class TokenInt(val value: BigInteger) : Token {
    override fun toString(): String = "${this::class.simpleName}(value=$value)"
}

class TokenFloat(val value: BigDecimal) : Token {
    override fun toString(): String = "${this::class.simpleName}(value=$value)"
}

class TokenBoolean(val value: Boolean) : Token {
    override fun toString(): String = "${this::class.simpleName}(value=$value)"
}

class TokenString(val value: String) : Token {
    override fun toString(): String = "${this::class.simpleName}(value='$value')"
}

class TokenIdentifier(val value: String) : Token {
    override fun toString(): String = "${this::class.simpleName}(value='$value')"
}
