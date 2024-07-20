package org.example.io.github.daniils_loputevs.rsql_runtime_executor.operators

import cz.jirutka.rsql.parser.ast.ComparisonOperator
import cz.jirutka.rsql.parser.ast.RSQLOperators
import java.time.LocalDateTime

fun main() {
    SingleArgumentOperatorImpl<LocalDateTime>(RSQLOperators.EQUAL) { LocalDateTime::equals.invoke(this, it) }
}


/**
 * TODO - что на счёт increment? как оно в RSQL?
 */
interface OperatorImpl<IDENTIFIER> {
    val operator: ComparisonOperator

    operator fun invoke(identifier: IDENTIFIER, arguments: List<Any?>): Boolean
}

class SingleArgumentOperatorImpl<IDENTIFIER>(
    override val operator: ComparisonOperator,
    val block: IDENTIFIER.(Any) -> Boolean
) : OperatorImpl<IDENTIFIER> {
    override fun invoke(identifier: IDENTIFIER, arguments: List<Any?>): Boolean = block(
        identifier,
        arguments.first() ?: throw KotlinNullPointerException("expected a single argument but got $arguments")
    )
}

class MultiArgumentOperatorImpl<IDENTIFIER>(
    override val operator: ComparisonOperator,
    val block: IDENTIFIER.(List<Any?>) -> Boolean
) : OperatorImpl<IDENTIFIER> {
    override fun invoke(identifier: IDENTIFIER, arguments: List<Any?>): Boolean = block(identifier, arguments)
}