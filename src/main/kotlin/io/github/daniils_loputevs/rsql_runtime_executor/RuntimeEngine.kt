package io.github.daniils_loputevs.rsql_runtime_executor

import cz.jirutka.rsql.parser.ast.ComparisonOperator
import org.example.io.github.daniils_loputevs.rsql_runtime_executor.operators.OperatorImpl
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * TODO - support custom Operators
 * TODO - support hierarhy property
 * TODO - support
 * TODO - support
 * TODO - support
 */
class RuntimeEngine {
    // val ClassReflectionScanner

    fun findGetterOrException(identifierPath : String, modelClazz : KClass<*> ) : KProperty.Getter<*> {
        TODO()
    }
    fun findIdentifierValueOrException(identifierPath : String, modelClazz : KClass<*> ) : Any? {
        TODO()
    }

    fun findOperatorImplOrException(operator : ComparisonOperator, identifierClass : KClass<*>) : OperatorImpl<Any> {
        TODO()
    }


}