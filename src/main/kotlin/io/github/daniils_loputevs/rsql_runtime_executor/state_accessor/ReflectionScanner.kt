package org.example.io.github.daniils_loputevs.rsql_runtime_executor.property_access

import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberExtensionProperties
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.superclasses

class ReflectionScanner(private val classVisitor: ClassVisitor = ClassVisitor()) {
    private val scannedClasses = mutableSetOf<KClass<*>>()


    fun scan(stateClass: KClass<*>): MutableClassStructure = stateClass
        .let { classVisitor.visit(it) }
        .also { scannedClasses += stateClass }


    /**
     * TODO - docs for full class contract
     */
    // DefaultClassStructureProvider
    open class ClassVisitor {

        fun visit(stateClass: KClass<*>): MutableClassStructure = stateClass.let {
            MutableClassStructure(
                stateClass,
                this.collectSuperClassHierarchy(it).toMutableList(),
                this.collectInterfacesHierarchy(it).toMutableSet(),
                this.collectDeclaredProperties(it).toMutableSet(),
                this.collectExtensionProperties(it).toMutableSet(),
                this.collectAddedProperties(it).toMutableSet()
            )
        }

        /**
         * Все супер классы как лист
         */
        fun collectSuperClassHierarchy(stateClass: KClass<*>): List<KClass<*>> {
            val stateClassSuperClass = stateClass.superclasses.firstOrNull { !it.java.isInterface }
            if (stateClassSuperClass == null) return emptyList()

            val superClasses = mutableListOf<KClass<*>>()
            val superClassesForWalk = mutableListOf<KClass<*>>()
            superClassesForWalk += stateClassSuperClass

            while (superClassesForWalk.isNotEmpty()) {
                val currClass = superClassesForWalk.removeFirst()
                superClasses += currClass

                val currSuper = currClass.superclasses.firstOrNull { !it.java.isInterface }
                if (currSuper == null) continue
                else superClassesForWalk += currSuper
            }
            return superClasses
        }

        /**
         * Все деревья всех interfaces как лист
         */
        fun collectInterfacesHierarchy(stateClass: KClass<*>): Set<KClass<*>> {
            val stateClassInterfaces = stateClass.superclasses.filter { it.java.isInterface }
            if (stateClassInterfaces.isEmpty()) return emptySet()

            val interfaces = mutableSetOf<KClass<*>>()
            val interfacesForWalk = mutableListOf<KClass<*>>()
            interfacesForWalk += stateClassInterfaces

            while (interfacesForWalk.isNotEmpty()) {
                val currClass = interfacesForWalk.removeFirst()
                interfaces += currClass

                val currInterfaces = currClass.superclasses.filter { it.java.isInterface }
                interfacesForWalk += currInterfaces
            }
            return interfaces
        }

        fun collectDeclaredProperties(stateClass: KClass<*>): Set<KProperty<*>> {
            return stateClass.declaredMemberProperties.toSet()
        }

        fun collectExtensionProperties(stateClass: KClass<*>): Set<KProperty<*>> {
            return stateClass.declaredMemberExtensionProperties.toSet()
        }

        fun collectAddedProperties(stateClass: KClass<*>): Set<KProperty<*>> {
//            TODO()
            return emptySet()
        }
    }
}