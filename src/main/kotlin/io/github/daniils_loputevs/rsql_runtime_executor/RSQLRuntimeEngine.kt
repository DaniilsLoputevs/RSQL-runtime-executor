package io.github.daniils_loputevs.rsql_runtime_executor

import cz.jirutka.rsql.parser.RSQLParser
import cz.jirutka.rsql.parser.ast.Node
import org.example.io.github.daniils_loputevs.rsql_runtime_executor.filter.FilterImpl
import java.util.*
import java.util.function.Predicate
import kotlin.reflect.KClass

fun main() {
    println("RSQLRuntimeEngine")

    val rootNode: Node = RSQLParser().parse("name==RSQL;version=ge=2.0")
}

//fun RSQLOperationApplier : RSQLRuntimeEngine

/**
 * ReflectionScanner
 * - operators [in, equals, compareTo]
 * - extension property
 *
 * Engine
 * - sort   - to comparator (tree)
 * - filter - to predicate  (tree)
 *
 * TODO : rename RSQLRuntimeEngine -> RSQLInMemoryExecutor
 */
class RSQLRuntimeEngine(
    val n : String = "",
    val filterImpl : FilterImpl,
) {

    /**
     * @return income [list] for method chain style
     */
    fun <ELEM> sortMutate(node: Node, list: MutableList<ELEM>): List<ELEM> =
        list.apply { if (checkSizeIsZero()) return this else sortWith(node.toComparator()) }

    /**
     * @return new List<ELEM> with sorted content
     */
    fun <ELEM> sort(node: Node, list: List<ELEM>): List<ELEM> =
        list.apply { if (checkSizeIsZero()) return this else sortedWith(node.toComparator()) }

    /**
     * @return income [list] for method chain style
     */
    fun <ELEM> filterMutate(node: Node, list: MutableList<ELEM>): MutableList<ELEM> =
//        list.apply { if (checkSizeIsZero()) return this else removeIf(node.toPredicate()) }
        list.apply { if (checkSizeIsZero()) return this else removeIf(filterImpl.nodeToPredicate(node)) }

    /**
     * @return new List<ELEM> with filtered content
     */
    fun <ELEM> filter(node: Node, list: List<ELEM>): List<ELEM> =
//        list.apply { if (checkSizeIsZero()) return this else filter { node.toPredicate<ELEM>().test(it) } }
        list.apply { if (checkSizeIsZero()) return this else filter {  filterImpl.nodeToPredicate<ELEM>(node).test(it) } }


    /* PRIVATE API */

    private fun <ELEM> List<ELEM>.checkSizeIsZero(): Boolean = this.isEmpty() || size == 1


    private fun <ELEM> Node.toPredicate(): Predicate<ELEM> = filterImpl.nodeToPredicate(this)





    /**
     * TODO ? OrNode - это типа == 0 ??? как эту ноду парсить?
     */
    private fun <ELEM> Node.toComparator(): Comparator<ELEM> {
        TODO()
    }


//    fun <ELEM> exe(node: Node, ctx: ExpressionContext<ELEM>): ExpressionResult<ELEM> {
//        when (node) {
//            is AndNode -> {}
//            is OrNode -> {}
//            is ComparisonNode -> {}
//        }
//    }

}

data class ExpressionContext<ELEM>(val obj: ELEM)

data class ExpressionResult<ELEM>(
    val value: ELEM,
    val valueType: KClass<*>,
    val exception: RuntimeException? = null
)