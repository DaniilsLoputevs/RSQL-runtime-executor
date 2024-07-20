package io.github.daniils_loputevs.rsql_runtime_executor.test_data

import cz.jirutka.rsql.parser.ast.*
import java.util.function.Predicate

fun main() {
//    val tree = AndNode(
//        ComparisonNode("A", ComparisonOperator("=="), listOf("A")),
//        OrNode(
//            ComparisonNode("B", ComparisonOperator("=="), listOf("B")),
//            AndNode(
//                ComparisonNode("C", ComparisonOperator("=="), listOf("C")),
//                ComparisonNode("D", ComparisonOperator("=="), listOf("D")),
//            )
//        )
//    )

    val data = TestDataSet {
        root = and {
            right = compare("A", "==", "A")
            left = or {
                right = compare("B", "==", "B")
                left = and {
                    right = compare("C", "==", "C")
                    left = compare("D", "==", "D")
                }
            }
        }
    }

    //    val predicateTree = nodeToPredicate(tree)
//    val predicateTree = nodeToPredicate2(data.rootAsRSQL())
//    println(predicateTree)
}


// TEST DSL
@DslMarker
annotation class RSQLTreeDSL

@RSQLTreeDSL
class TestDataSet {
    lateinit var root: TestNode
    @RSQLTreeDSL fun and(init: TestAndNode.() -> Unit): TestAndNode = TestAndNode().apply(init)
    @RSQLTreeDSL fun or(init: TestOrNode.() -> Unit): TestOrNode = TestOrNode().apply(init)
    @RSQLTreeDSL fun compare(identifier: String, operator: String, statement: String):
            TestCompareNode = TestCompareNode(identifier, operator, statement)

    fun rootAsRSQL(): Node = this.root.toRSQL()

    fun <ELEM> rootAsPredicateTree(): Predicate<ELEM> = TODO("Impl")
}

/**
 * Yes recursion but expect that test date tree will not be so big to produce SOF.
 */
private fun TestNode.toRSQL(): Node = when (this) {
    is TestAndNode -> AndNode(listOf(this.left.toRSQL(), this.right.toRSQL()))
    is TestOrNode -> OrNode(listOf(this.left.toRSQL(), this.right.toRSQL()))
    is TestCompareNode -> ComparisonNode(
        RSQLOperators.defaultOperators().find { it.symbol == this.operator },
        this.identifier,
        listOf(this.statement)
    )
    else -> throw RuntimeException("failed to convert $this to RSQL")
}


fun TestDataSet(init: TestDataSet.() -> Unit) = TestDataSet().apply(init)

@RSQLTreeDSL interface TestNode
@RSQLTreeDSL interface TestLogicNode : TestNode {
    var right: TestNode
    var left: TestNode


    @RSQLTreeDSL fun TestLogicNode.and(init: TestAndNode.() -> Unit): TestAndNode = TestAndNode().apply(init)
    @RSQLTreeDSL fun TestLogicNode.or(init: TestOrNode.() -> Unit): TestOrNode = TestOrNode().apply(init)
    @RSQLTreeDSL fun TestLogicNode.compare(identifier: String, operator: String, statement: String):
            TestCompareNode = TestCompareNode(identifier, operator, statement)
}

@RSQLTreeDSL class TestAndNode : TestLogicNode {
    override lateinit var right: TestNode
    override lateinit var left: TestNode
}

@RSQLTreeDSL class TestOrNode : TestLogicNode {
    override lateinit var right: TestNode
    override lateinit var left: TestNode
}

@RSQLTreeDSL class TestCompareNode(
    val identifier: String,
    val operator: String,
    val statement: String
) : TestNode
