package io.github.daniils_loputevs.rsql_runtime_executor.test_data


/* === GRANT PARENT === */

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