package org.example.io.github.daniils_loputevs.rsql_runtime_executor.filter

import cz.jirutka.rsql.parser.ast.*
import io.github.daniils_loputevs.rsql_runtime_executor.RuntimeEngine
import io.github.daniils_loputevs.rsql_runtime_executor.filter.*
import java.util.function.Predicate

class FilterImpl(private val engine: RuntimeEngine) {
    private val LogicalNode.left: Node
        get() = this.children[0] ?: throw RuntimeException("Expected left node for node $this")
    private val LogicalNode.right: Node
        get() = this.children[1] ?: throw RuntimeException("Expected right node for node $this")
//val List<Predicate>.firstOrException : Predicate get() = this.firstOrNull() ?: throw RuntimeException("Expected first node for node ${this::class.simpleName}")

    fun <ELEM> nodeToPredicate(root: Node): Predicate<ELEM> {
        /* (currNode & parentNode) // then currNode == root => parent == null */
        val stack = mutableListOf<Pair<Node, PredicateTreeNode<*>?>>()
        var rootPredicate: Predicate<ELEM>? = null
        stack.add(root to null)

        while (stack.isNotEmpty()) {
            val (curr, parent) = stack.removeAt(stack.lastIndex)
            val currPredicate: Predicate<Any?>

            when (curr) {
                is AndNode -> {
                    currPredicate = AndPredicate(curr)
                    stack.add(curr.right to currPredicate)
                    stack.add(curr.left to currPredicate)
                }
                is OrNode -> {
                    currPredicate = OrPredicate(curr)
                    stack.add(curr.right to currPredicate)
                    stack.add(curr.left to currPredicate)
                }
                is ComparisonNode -> {
                    currPredicate = ComparePredicate(curr) { listElement: Any? ->
                        // compile : listElem
                        // compile : compareNode (left_id, operator, right_id)
                        // runtime : list_elem_class
                        // runtime : left_value, operator_impl, right_value,
                        // >> boolean

                        // TODO - что делать есть list_elem_class == null ?????
//                        val elemClazz = listElement::class
//                        val leftValue = engine.findIdentifierValueOrException(curr.selector, elemClazz)
//                        val operatorImpl = engine.findOperatorImplOrException(curr.operator, elemClazz)
//                        val rightValues = curr.arguments.map { engine.findIdentifierValueOrException(it, elemClazz) }

//                        if (curr.operator.isMultiValue) {
//
//                            val rightValues = curr.arguments.map {engine.findIdentifierValueOrException(it, elemClazz)}
//                        } else {
//
//                        }

                        TODO("impl normal compare")
                        listElement.toString() == "currentNode.arguments.first()"
                    }
                }
                else -> throw RuntimeException("fail to parse root node because unknown node type ${root.javaClass}")

            }
            when (parent) {
                null -> {} // do nothing because parent is root node
                is PredicateTreeLogicalNode -> parent.addChild(currPredicate)
                else -> throw RuntimeException("Unexpected parent type! Parent must inherit from ${PredicateTreeLogicalNode::class.java.simpleName}")
            }
            if (rootPredicate == null) rootPredicate = currPredicate as Predicate<ELEM>
        }
        return rootPredicate ?: throw RuntimeException("fail to parse root node because root node can't be null")
    }
}