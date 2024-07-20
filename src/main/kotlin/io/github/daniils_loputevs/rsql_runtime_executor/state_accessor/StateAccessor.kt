package org.example.io.github.daniils_loputevs.rsql_runtime_executor.property_access

import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class StateAccessor(
    private val classRegistry: ClassStructureRegistry = ClassStructureRegistry()
) {

    /**
     * TODO - что если state is null ?
     */
    fun access(path: String, state: Any?): Any? {
        if (state == null) throw RuntimeException("can't access value by path=$path of state=null")
        if (path.isBlank()) throw IllegalArgumentException("path cannot be blank")

        val stateClass = state::class
        val stateSelector = StateSelector(path, path.split(".").toMutableList())

        return try {
            this.accessImpl(stateSelector, state, stateClass)
        } catch (e: Exception) {
            throw RuntimeException("can't access value by path='$path' of state='$stateClass'", e)
        }
    }

    /**
     * TODO - list & map syntax - check expected and actual types
     */
    private fun accessImpl(stateSelector: StateSelector, state: Any, stateClass: KClass<*>): Any? {
        val walkedPath = mutableSetOf<String>()
        var currValue: Any? = null
        var currClass: KClass<*> = stateClass
        for ((index, currPropertyName) in stateSelector.elements.withIndex()) {

            val currProperty = classRegistry[currClass, currPropertyName]

            currValue = currProperty.tryCatchCall(state) {
                "Fail access to Property! Property getter throw exception! " +
                        "property=$currPropertyName, path=${stateSelector.path}, walkedPath=${walkedPath.joinToString()}"
            }

            // if we meet null in middle of path
            if (currValue == null && index < stateSelector.elements.lastIndex) throw NullPointerException(
                "Fail access to Property! expected: '${walkedPath.joinToString()}' is not null but actual it's null! " +
                        "property=$currPropertyName, path=${stateSelector.path}, walkedPath=${walkedPath.joinToString()}"
            )

            // todo - разобраться какого хрена и зачем?
            currClass = if (index == stateSelector.elements.lastIndex) currValue!!::class
            else throw RuntimeException(
                "Fail access to property=$currPropertyName, path=${stateSelector.path}, " +
                        "walkedPath=${walkedPath.joinToString()}"
            )

            walkedPath += currPropertyName
        }
        return currValue
    }

    private fun KProperty<*>.tryCatchCall(state: Any, errMsg: () -> String): Any? = try {
        this.getter.call(state)
    } catch (e: Exception) {
        throw RuntimeException(errMsg(), e)
    }

}

