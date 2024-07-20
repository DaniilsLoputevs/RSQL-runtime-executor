package io.github.daniils_loputevs.rsql_runtime_executor.filter

import cz.jirutka.rsql.parser.ast.OrNode
import java.util.function.Predicate

data class OrPredicate(
    override val origin: OrNode,
    override var left: Predicate<Any?>? = null,
    override var right: Predicate<Any?>? = null
) : PredicateTreeLogicalNode<OrNode> {

    override fun test(t: Any?): Boolean {
        validate()
        return left!!.test(t) || right!!.test(t)
    }

    override fun toString(): String = "OrPredicate(left=$left, right=$right)"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrPredicate

        if (left != other.left) return false
        if (right != other.right) return false

        return true
    }

    override fun hashCode(): Int {
        var result = left?.hashCode() ?: 0
        result = 31 * result + (right?.hashCode() ?: 0)
        return result
    }

}