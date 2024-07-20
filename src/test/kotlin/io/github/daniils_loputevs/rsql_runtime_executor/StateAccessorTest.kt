package io.github.daniils_loputevs.rsql_runtime_executor

import io.github.daniils_loputevs.rsql_runtime_executor.test_data.USER_DEFAULT
import org.example.io.github.daniils_loputevs.rsql_runtime_executor.property_access.StateAccessor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 *
 * Glossary:
 * declared = declared in this class
 * --- extension = all public extension properties for this class
 * added = added from config to this class
 * inherited = inherited from super-class hierarchy or interfaces
 *
 * Support types from box:
 * - support primitive extension : [BigInteger, BigDecimal]
 * - date & time : all classes
 * - Map || Array || List : roles[]
 * -
 */
class StateAccessorTest {
    val user = USER_DEFAULT
    val api = StateAccessor()

    private infix fun String.test(expected: Any?) = assertEquals(expected, api.access(this, user))
    private infix fun String.testHierarchy(expected: Any?) = assertEquals(expected, api.access(this, hierarchy))

    @Test fun test_syntax_this() = "this" test user
    @Test fun test_syntax_boolean() = "true" test true
    @Test fun test_syntax_int() = "312" test 312
    @Test fun test_syntax_double() = "312.17" test 312.17
    @Test fun test_syntax_string() = "\"success\"" test "success"
    @Test fun test_syntax_List() = "roles[1]" test user.roles[1]
    @Test fun test_syntax_List_Nested_expression() = "roles[shortValue]" test user.roles[1]
    @Test fun test_syntax_Map() = "attributes[\"email\"]" test user.attributes["email"]
    @Test fun test_syntax_Map_Nested_expression() = "attributes[tokenType]" test user.attributes[user.tokenType]
    @Test fun test_syntax_nested_Object() = "loginInfo.lastLogin" test user.loginInfo.lastLogin

//    @Test fun test_declared() = "salary" test user.salary
//    @Test fun test_super_class() = "id" test user.id
//    @Test fun test_super_class_super_class() = "registeredAt" test user.registeredAt
//    @Test fun test_impl_interface() = "name" test user.name

    // interface properties hierarchy

    /**
     * Test interface hierarchy graph
     *```
     *                                GP2AA
     *                                /    \
     *            GP1A    GP1B     GP2A   GP2B
     *               \   /            \   /
     * GrandParent :  GP1      +       GP2    +     GP3
     *   |
     * Parent : P1
     *   |
     * Child : C1
     */
    private val hierarchy = Child()

    @Test fun test_gp1() = "gp1" testHierarchy hierarchy.gp1
    @Test fun test_gp1a() = "gp1a" testHierarchy hierarchy.gp1a
    @Test fun test_gp1b() = "gp1b" testHierarchy hierarchy.gp1b

    @Test fun test_gp2() = "gp2" testHierarchy hierarchy.gp2
    @Test fun test_gp2a() = "gp2a" testHierarchy hierarchy.gp2a
    @Test fun test_gp2b() = "gp2b" testHierarchy hierarchy.gp2b
    @Test fun test_gp2aa() = "gp2aa" testHierarchy hierarchy.gp2aa

    @Test fun test_gp3() = "gp3" testHierarchy hierarchy.gp3

    @Test fun test_grant_parent() = "grantParent" testHierarchy hierarchy.grantParent
    @Test fun test_parent() = "parent" testHierarchy hierarchy.parent
    @Test fun test_child() = "child" testHierarchy hierarchy.child

    @Test fun test_p1() = "p1" testHierarchy hierarchy.p1
    @Test fun test_p2() = "c1" testHierarchy hierarchy.c1


    @Test fun run_debug() {
        val path = "displayName"
//        val expected = USER_DEFAULT.displayName
//        assertEquals(expected, api.access(path, user))
    }


    /*
    - this syntax ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    - boolean syntax ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    - int constant syntax ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    - decimal constant syntax ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    - string constant syntax ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    - list syntax ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    - list syntax + nested expression ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    - map syntax + string const value ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    - map syntax + nested expression ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    - nested object syntax ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

    - decimal constant - how to parse decimal?
    - ? ENUM constant - ? search reflection : public static ENUM_TYPE valueOf(String str)
    - added constant

    - declared field ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    - field from super ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    - field from super impl interface ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    - field from super-super ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    - field from interface [1]
    - field from interface [1] impls interface [A]
    - field from interface [2]
    - field from interface [2] impls interface [B]
    - added field

    - kotlin getters
    - java getters
     */


    open class GrantParent : GP1, GP2, GP3 {
        val grantParent: String = "GrantParent"
    }

    interface GP1A {
        val gp1a: String get() = "gp1a"
    }

    interface GP1B {
        val gp1b: String get() = "gp1b"
    }

    interface GP1 : GP1A, GP1B {
        val gp1: String get() = "gp1"
    }

    interface GP2AA {
        val gp2aa: String get() = "gp2aa"
    }

    interface GP2A : GP2AA {
        val gp2a: String get() = "gp2a"
    }

    interface GP2B : GP2AA {
        val gp2b: String get() = "gp2b"
    }

    interface GP2 : GP2A, GP2B {
        val gp2: String get() = "gp2"
    }

    interface GP3 {
        val gp3: String get() = "gp3"
    }

    /* === PARENT === */

    open class Parent : P1, GrantParent() {
        val parent: String = "parent"
    }

    interface P1 {
        val p1: String get() = "p1"
    }

    /* === CHILD === */


    class Child : C1, Parent() {
        val child: String = "child"
    }

    interface C1 {
        val c1: String get() = "c1"
    }
}