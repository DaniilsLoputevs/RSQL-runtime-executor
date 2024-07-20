package io.github.daniils_loputevs.rsql_runtime_executor.filter

import cz.jirutka.rsql.parser.ast.ComparisonNode
import cz.jirutka.rsql.parser.ast.ComparisonOperator
import java.util.function.Predicate

data class ComparePredicate(override val origin: ComparisonNode, val condition: (Any?) -> Boolean) :
    PredicateTreeNode<ComparisonNode> {

    val leftIdentifier: String get() = origin.selector
    val operator: ComparisonOperator get() = origin.operator
    val rightIdentifier: String get() = origin.arguments.joinToString()

    override fun test(t: Any?): Boolean = condition(t)


    override fun toString(): String = "$origin"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ComparePredicate

        if (leftIdentifier != other.leftIdentifier) return false
        if (operator != other.operator) return false
        if (rightIdentifier != other.rightIdentifier) return false

        return true
    }

    override fun hashCode(): Int {
        var result = leftIdentifier.hashCode()
        result = 31 * result + operator.hashCode()
        result = 31 * result + rightIdentifier.hashCode()
        return result
    }

}