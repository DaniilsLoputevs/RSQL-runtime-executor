package org.example.io.github.daniils_loputevs.rsql_runtime_executor.property_access

import kotlin.reflect.KClass
import kotlin.reflect.KProperty

open class ClassStructure(
    val clazz: KClass<*>,
    open val superClassesHierarchy: List<KClass<*>>,
    open val interfacesHierarchy: Set<KClass<*>>,
    open val declaredProperties: Set<KProperty<*>>,
    open val extensionProperties: Set<KProperty<*>>,
    open val addedProperties: Set<KProperty<*>>,
) {

    fun findProperty( propertyName: String): KProperty<*>? {
        this.declaredProperties.find { it.name == propertyName }?.let { return it }
        this.extensionProperties.find { it.name == propertyName }?.let { return it }
        this.addedProperties.find { it.name == propertyName }?.let { return it }
        return null // return null if property not found
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ClassStructure
        return clazz == other.clazz
    }

    override fun hashCode(): Int = clazz.hashCode()
    override fun toString(): String = "ClassStructure(clazz=$clazz)"
}

class MutableClassStructure(
    clazz: KClass<*>,
    override val superClassesHierarchy: MutableList<KClass<*>> = mutableListOf(),
    override val interfacesHierarchy: MutableSet<KClass<*>> = mutableSetOf(),
    override val declaredProperties: MutableSet<KProperty<*>> = mutableSetOf(),
    override val extensionProperties: MutableSet<KProperty<*>> = mutableSetOf(),
    override val addedProperties: MutableSet<KProperty<*>> = mutableSetOf(),
) : ClassStructure(
    clazz,
    superClassesHierarchy,
    interfacesHierarchy,
    declaredProperties,
    extensionProperties,
    addedProperties,
) {
    fun toImmutable(): ClassStructure = ClassStructure(
        clazz,
        superClassesHierarchy.toList(),
        interfacesHierarchy.toSet(),
        declaredProperties.toSet(),
        extensionProperties.toSet(),
        addedProperties.toSet(),
    )
}