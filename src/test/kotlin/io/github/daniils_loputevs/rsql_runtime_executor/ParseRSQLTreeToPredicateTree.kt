package io.github.daniils_loputevs.rsql_runtime_executor

import io.github.daniils_loputevs.rsql_runtime_executor.test_data.TestDataSet
import io.github.daniils_loputevs.rsql_runtime_executor.test_data.User
import org.example.io.github.daniils_loputevs.rsql_runtime_executor.filter.FilterImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class ParseRSQLTreeToPredicateTree {
    val filterImpl by lazy { FilterImpl(RuntimeEngine()) }

    /**
     * 1 - описать RSQL query (String || DSL -> Strong)
     * 2 - RSQL query -> RSQL Node(tree)
     * 3 - TEST : RSQL Node(tree) -> Predicate Node(tree)
     * 4 - TEST : описать expected Predicate Node(tree)
     * 4 - TEST : сравнить expected && actual Predicate Node(tree)
     */
    @Test fun test() {
        val data = TestDataSet {
            root = and {
                right = compare("A", "==", "A")
                left = or {
                    right = compare("B", "==", "B")
                    left = and {
                        right = compare("C", "==", "C")
                        left = compare("D", "==", "D")
                    }
                }
            }
        }
        val expect = data.rootAsPredicateTree<User>()
        val actual = filterImpl.nodeToPredicate<User>(data.rootAsRSQL())
        assertEquals(expect, actual)
    }
}