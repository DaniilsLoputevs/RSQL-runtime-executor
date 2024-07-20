package org.example.io.github.daniils_loputevs.rsql_runtime_executor.operators

import cz.jirutka.rsql.parser.ast.ComparisonOperator
import cz.jirutka.rsql.parser.ast.RSQLOperators
import kotlin.reflect.KClass
import kotlin.reflect.jvm.internal.impl.metadata.jvm.deserialization.JvmMemberSignature.Method

fun main() {
    registry {

//        validations {
//            path {path -> }
//        }

        Unit::class {
            operators {
                RSQLOperators.IN implN { args -> true }
            }
            properties {
                "fieldName" getter { toString() }
            }
//            val o = RSQLOperators.IN to { int: Int -> }
        }
//         Void::class .operatorImpl<Void> { }
    }


    val o = { int: Int -> }
    val r = { str: String, int: Int, met: Method -> }
    o::class.isFun
    println(o)
}

// o::class.java.interfaces
// r::class.java.methods
@ConfigDSL fun registry(block: RegistryDslElement.() -> Unit): RegistryDslElement = RegistryDslElement().apply(block)


//fun KClass<*>.operatorImpl(block: () -> Unit) {}

//operator fun

@DslMarker annotation class ConfigDSL

@ConfigDSL class RegistryDslElement {
    internal val classDescriptions: MutableList<ClassDescriptionDslElement<*>> = mutableListOf()
    internal val validations: ValidationsDslElement = ValidationsDslElement()

    @ConfigDSL operator fun <CLASS : Any> KClass<CLASS>.invoke(block: ClassDescriptionDslElement<CLASS>.() -> Unit) =
        ClassDescriptionDslElement(this).apply(block).also { classDescriptions += it }

    @ConfigDSL fun validations(init: ValidationsDslElement.() -> Unit) = validations.apply(init)
}

@ConfigDSL class ClassDescriptionDslElement<CLASS : Any>(val clazz: KClass<CLASS>) {
    internal val properties: MutableList<PropertiesDslElement<*>> = mutableListOf()

    @ConfigDSL fun properties(init: PropertiesDslElement<CLASS>.() -> Unit): PropertiesDslElement<CLASS> =
        PropertiesDslElement(clazz).apply(init).also { properties += it }


    @ConfigDSL fun operators(init: OperatorDslElement.() -> Unit): OperatorDslElement = OperatorDslElement().apply(init)

}

@ConfigDSL class ValidationsDslElement {
    val pathChecks = mutableListOf<(String) -> Boolean>()

    @ConfigDSL fun path(validation: (String) -> Boolean): Unit = run { pathChecks += validation }

}

@ConfigDSL class OperatorDslElement {
    @ConfigDSL infix fun ComparisonOperator.impl(impl: () -> Boolean) {
        TODO("Impl")
    }

    @ConfigDSL infix fun ComparisonOperator.impl1(impl: (Any) -> Boolean) {
        TODO("Impl")
    }

    @ConfigDSL infix fun ComparisonOperator.impl2(impl: (Any, Any) -> Boolean) {
        TODO("Impl")
    }

    @ConfigDSL infix fun ComparisonOperator.implN(impl: (List<Any>) -> Boolean) {
        TODO("Impl")
    }
}

@ConfigDSL class PropertiesDslElement<CLASS : Any>(val clazz : KClass<CLASS>) {
    val fieldDescriptions : MutableList<FieldDescription<*, *>> = mutableListOf()

    @ConfigDSL inline infix fun <reified RETURN : Any> String.getter(noinline getter: CLASS.() -> RETURN) {
        fieldDescriptions += FieldDescription(clazz, this, RETURN::class, getter)
    }

    data class FieldDescription<CLASS : Any, RETURN : Any>(
        val clazz: KClass<CLASS>,
        val name  : String,
        val type : KClass<RETURN>,
        val getter : CLASS.() -> RETURN)
}