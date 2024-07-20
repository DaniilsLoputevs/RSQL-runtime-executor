package org.example.io.github.daniils_loputevs.rsql_runtime_executor.property_access

import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class ClassStructureRegistry {
    private val clasStructureCache = mutableMapOf<KClass<*>, ClassStructure>()
    private val reflectionScanner = ReflectionScanner()
    private val dslContainer: Any = "" // TODO

    operator fun get(stateClass: KClass<*>, propertyName: String): KProperty<*> =
        findPropertyInHierarchy(this.getOrLoad(stateClass), propertyName)
            ?: throw NoSuchElementException("property='$propertyName' not found in full hierarchy for clas='${stateClass.qualifiedName}'")


    /**
     * Loading properties by Reflection API
     *
     * @return return [ClassStructure] from cache or load then return
     */
    private fun getOrLoad(stateClass: KClass<*>): ClassStructure =
        clasStructureCache[stateClass] ?: this.loadCascade(stateClass)

    private fun loadCascade(stateClass: KClass<*>): ClassStructure {
        val kClassesForWalk = LinkedList<KClass<*>>()
        val stateStruct = this.load(stateClass).also {
            it.superClassesHierarchy.forEach(kClassesForWalk::addLast)
            it.interfacesHierarchy.forEach(kClassesForWalk::addLast)
        }

        while (kClassesForWalk.isNotEmpty()) {
            this.load(kClassesForWalk.removeFirst())
                .also { it.interfacesHierarchy.forEach(kClassesForWalk::addLast) }
        }
        return stateStruct
    }

    private fun load(stateClass: KClass<*>): ClassStructure =
        reflectionScanner.scan(stateClass)
            .also { struct -> struct.addedProperties.addAll(this.getAddedPropertyFromDslByClass(struct.clazz)) }
            .toImmutable()
            .also { struct -> clasStructureCache[struct.clazz] = struct }

    /**
     * подгрузка properties из DSL
     */
    fun getOrLoad(dslContext: Any): Boolean {
        TODO()
    }

    /**
     * Find Property by name in structure
     * Search in by priority:
     * - this class declaration
     * - all super classes declaration
     * - all interfaces tree of this class and each super class
     *
     * @return return null if property not found
     */
    private fun findPropertyInHierarchy(structure: ClassStructure, propertyName: String): KProperty<*>? {
        val walkStructure = LinkedList<ClassStructure>()
        walkStructure.addFirst(structure)
        structure.superClassesHierarchy
            .asSequence()
            .map { superClass -> this.clasStructureCache[superClass] }
            .forEach { superClassStructure -> walkStructure.addLast(superClassStructure) } // dont' forget to terminate sequence

        while (walkStructure.isNotEmpty()) {
            walkStructure.removeFirst().run {
                this.findProperty(propertyName)?.let { property -> return property } // return from method

                this.interfacesHierarchy
                    .asSequence()
                    .map { superClass -> this@ClassStructureRegistry.clasStructureCache[superClass] }
                    .forEach { superClassStructure -> walkStructure.addLast(superClassStructure) }
            }
        }
        return null // return null if property not found
    }


    // todo
    private fun getAddedPropertyFromDslByClass(clazz: KClass<*>): List<KProperty<*>> = emptyList() // todo

}