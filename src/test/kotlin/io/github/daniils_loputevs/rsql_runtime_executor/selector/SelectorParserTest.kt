package io.github.daniils_loputevs.rsql_runtime_executor.selector

import org.example.io.github.daniils_loputevs.rsql_runtime_executor.selector.SelectorParser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


//           "----.----.----[----.---.['1234'].---]"
//           1------------1                           \
//                         2--------2        2----2   \
//                                   3------3         \
class SelectorParserTest {

    val input = "user.info.extra.counter[user.info.last].start[\"1234\"]['collectAttrs'].continue"

    @Test fun test1() = println(SelectorParser().parse(input).let { println(it) })


    operator fun get(a : Any, b : Any) : String = ""
}