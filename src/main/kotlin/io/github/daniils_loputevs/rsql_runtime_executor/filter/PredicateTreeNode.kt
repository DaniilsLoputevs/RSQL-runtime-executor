package io.github.daniils_loputevs.rsql_runtime_executor.filter

import cz.jirutka.rsql.parser.ast.Node
import java.util.function.Predicate

/**
 * TODO : docs - этот interface нужен для обобщения всех элементов Predicate tree.
 *          Что бы, была возможность tree walk by predicate nodes as one interface
 */
interface PredicateTreeNode<ORIGIN : Node> : Predicate<Any?> {
    val origin: ORIGIN
}

interface PredicateTreeLogicalNode<ORIGIN : Node> : PredicateTreeNode<ORIGIN> {
    var left: Predicate<Any?>?
    var right: Predicate<Any?>?

    fun addChild(predicate: Predicate<Any?>) {
        if (left == null) left = predicate
        else if (right == null) right = predicate
        else throw IllegalArgumentException("Impossible to add a node because the Left and Right nodes are already set!")
    }

    fun validate() {
        if (left != null) throw RuntimeException("this.left : Predicate - must be not null")
        if (right != null) throw RuntimeException("this.right : Predicate - must be not null")
    }

}