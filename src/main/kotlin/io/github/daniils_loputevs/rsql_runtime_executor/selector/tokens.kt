package org.example.io.github.daniils_loputevs.rsql_runtime_executor.selector

import java.math.BigDecimal
import java.math.BigInteger


typealias AnyToken = Token<*>

sealed class Token<CONTENT>(val value: CONTENT) {

    override fun toString(): String = "${this::class.simpleName}(value=$value})"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Token<*>) return false // check hierarchy(is $other a sub-class of Token) as well
        if (value != other.value) return false
        return true
    }

    override fun hashCode(): Int = value?.hashCode() ?: 0

}

open class Expression(tokens: MutableList<AnyToken> = mutableListOf()) : Token<MutableList<AnyToken>>(tokens)
//    , Iterable<Token<*>>
{

    operator fun plusAssign(token: AnyToken) = Unit.also { value.add(token) }
//    override fun iterator(): Iterator<Token<*>> = tokens.iterator()
}

class IndexAccess(tokens: MutableList<AnyToken> = mutableListOf()) : Expression(tokens)
data object TokenThis : Token<Unit>(Unit)
data object TokenNull : Token<Unit?>(null)
class TokenInt(value: BigInteger) : Token<BigInteger>(value)
class TokenFloat(value: BigDecimal) : Token<BigDecimal>(value)
class TokenBoolean(value: Boolean) : Token<Boolean>(value)
class TokenString(value: String) : Token<String>(value)
class TokenIdentifier(value: String) : Token<String>(value)
