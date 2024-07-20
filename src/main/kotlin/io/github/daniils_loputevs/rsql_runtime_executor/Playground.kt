//package io.github.daniils_loputevs.rsql_runtime_executor
//
//import cz.jirutka.rsql.parser.ast.*
//import cz.jirutka.rsql.parser.ast.AndNode
//import cz.jirutka.rsql.parser.ast.ComparisonNode
//import cz.jirutka.rsql.parser.ast.OrNode
//import java.time.LocalDateTime
//import java.util.function.Predicate
//

//

//
//
//val LogicalNode.left: Node
//    get() = this.children[0] ?: throw RuntimeException("Expected left node for node ${this::class.simpleName}")
//val LogicalNode.right: Node
//    get() = this.children[1] ?: throw RuntimeException("Expected right node for node ${this::class.simpleName}")
////val List<Predicate>.firstOrException : Predicate get() = this.firstOrNull() ?: throw RuntimeException("Expected first node for node ${this::class.simpleName}")
//
//fun nodeToPredicate2(root: Node): Predicate<Any> {
//    /* (currNode & parentNode) // then currNode == root => parent == null */
//    val stack = mutableListOf<Pair<Node, PredicateTreeNode<*>?>>()
//    var rootPredicate: Predicate<Any>? = null
//    stack.add(root to null)
//
//    while (stack.isNotEmpty()) {
//        val (curr, parent) = stack.removeAt(stack.lastIndex)
//        val currPredicate: Predicate<Any>
//
//        when (curr) {
//            is AndNode -> {
//                currPredicate = AndPredicate2(curr)
//                parent?.addChild(currPredicate)
//                stack.add(curr.right to currPredicate)
//                stack.add(curr.left to currPredicate)
//            }
//            is OrNode -> {
//                currPredicate = OrPredicate2(curr)
//                parent?.addChild(currPredicate)
//                stack.add(curr.right to currPredicate)
//                stack.add(curr.left to currPredicate)
//            }
//            is ComparisonNode -> {
//                currPredicate = ComparePredicate2(curr) { value: Any ->
//                    TODO("impl normal compare")
//                    value.toString() == "currentNode.arguments.first()"
//                }
//                parent?.addChild(currPredicate)
//            }
//            else -> throw RuntimeException("fail to parse root node because unknown node type ${root.javaClass}")
//        }
//        if (rootPredicate == null) rootPredicate = currPredicate
//    }
//    return rootPredicate ?: throw RuntimeException("fail to parse root node because root node can't be null")
//}
//
////var ьфз = mutableMapOf<String, String>()
////
////recordFindMetricsData(afterTime, MessageType.SMS.name, successStatusList)
////recordFindMetricsData(afterTime, MessageType.SMS.name, serverErrorStatusList)
////recordFindMetricsData(afterTime, MessageType.SMS.name, dataErrorStatusList)
////
////recordFindMetricsData(afterTime, MessageType.EMAIL.name, successStatusList)
////recordFindMetricsData(afterTime, MessageType.EMAIL.name, serverErrorStatusList)
////recordFindMetricsData(afterTime, MessageType.EMAIL.name, dataErrorStatusList)
////
////innerFindMetricsData(afterTime, MessageType.SMS.name, successStatusList, innerSendSmsSuccessAtomic)
////innerFindMetricsData(afterTime, MessageType.SMS.name, serverErrorStatusList, innerSendSmsServerErrorsAtomic)
////innerFindMetricsData(afterTime, MessageType.SMS.name, dataErrorStatusList, innerSendSmsDataErrorsAtomic)
////
////innerFindMetricsData(afterTime, MessageType.EMAIL.name, successStatusList, innerSendEmailSuccessAtomic)
////innerFindMetricsData(afterTime, MessageType.EMAIL.name, serverErrorStatusList, innerSendEmailServerErrorsAtomic)
////innerFindMetricsData(afterTime, MessageType.EMAIL.name, dataErrorStatusList, innerSendEmailDataErrorsAtomic)
//
//fun dsl(afterTime : LocalDateTime) {
//    afterTime.metric(innerRepo, MessageType.SMS)
//    afterTime.metric(innerRepo, MessageType.EMAIL)
//
//    afterTime.metric(externalRepo, MessageType.EMAIL)
//    afterTime.metric(externalRepo, MessageType.EMAIL)
//}
//
//fun LocalDateTime.metric(repo : Any, type : Any) {
//    repo.invoke(afterTime, type.name, successStatusList)
//    repo.invoke(afterTime, type.name, serverErrorStatusList)
//    repo.invoke(afterTime, type.name, dataErrorStatusList)
//}
//
//
