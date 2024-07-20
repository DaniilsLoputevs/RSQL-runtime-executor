package io.github.daniils_loputevs.rsql_runtime_executor.filter

import cz.jirutka.rsql.parser.ast.AndNode
import java.util.function.Predicate

data class AndPredicate(
    override val origin: AndNode,
    override var left: Predicate<Any?>? = null,
    override var right: Predicate<Any?>? = null
) : PredicateTreeLogicalNode<AndNode> {

    override fun test(t: Any?): Boolean {
        validate()
        return left!!.test(t) && right!!.test(t)
    }


    override fun toString(): String = "AndPredicate(left=$left, right=$right)"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AndPredicate

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