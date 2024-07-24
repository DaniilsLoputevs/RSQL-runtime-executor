package io.github.daniils_loputevs.rsql_runtime_executor.selector

import org.example.io.github.daniils_loputevs.rsql_runtime_executor.selector.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


//           "----.----.----[----.---.['1234'].---]"
//           1------------1                           \
//                         2--------2        2----2   \
//                                   3------3         \
class SelectorParserTest {
    private val parser : SelectorParser = SelectorParser()


    private infix fun id(value: String) = TokenIdentifier(value)
    private infix fun str(value: String) = TokenString(value)
    private infix fun int(value: Int) = TokenInt(value.toBigInteger())
    private infix fun float(value: Double) = TokenFloat(value.toBigDecimal())
    private fun indexAccess(vararg tokens: Token<*>) = IndexAccess(mutableListOf(*tokens))

    private infix fun String.test(tokens: List<AnyToken>): Unit = parser.parse(this).value
//        .also { println(it) }
        .let { assertEquals(tokens, it) }


    /* === TESTS === */


    @Test fun complex() = "user.info.extra.counter[user.info.last].start[\"abc\"]['collectAttrs'][this].continue" test listOf(
        id("user"), id("info"), id("extra"), id("counter"),
        indexAccess(
            id("user"), id("info"), id("last")
        ),
        id("start"),
        indexAccess(str("abc")),
        indexAccess(str("collectAttrs")),
        indexAccess(TokenThis),
        id("continue")
    )

    @Test fun identifierPath() = "user.info.extra" test
            listOf(id("user"), id("info"), id("extra"))


    // === INDEX ACCESS ===


    @Test fun indexAccessWithPath() = "[user.info.last]" test
            listOf(indexAccess(id("user"), id("info"), id("last")))

    @Test fun indexAccessWithPathAndPathAfter() = "[user.info.last].one.two" test
            listOf(
                indexAccess(id("user"), id("info"), id("last")),
                id("one"), id("two")
            )

    @Test fun indexAccessWithPathWithNestedAccessAndPathAfter() = "[user.info[14].last].one.two" test
            listOf(
                indexAccess(id("user"), id("info"), indexAccess(int(14)), id("last")),
                id("one"), id("two")
            )

    //                                                              Index access ranges
    //                                          1 ---------------------------------------------------------;
    //                                                    1.1-------;     1.2------------------------;
    //                                                                               1.2.1:  1.2.2--;
    private val manyNextedAccesses = "dto.prices[dto.users['aaa-bbb'].roles[ROLES_LIST[0]['customer']].last].one.two"
    @Test fun indexAccessManyNestedAccesses() = manyNextedAccesses test
            listOf(
                id("dto"), id("prices"), indexAccess(
                    id("dto"), id("users"), indexAccess(
                        str("aaa-bbb")
                    ),
                    id("roles"), indexAccess(
                        id("ROLES_LIST"),
                        indexAccess(int(0)),
                        indexAccess(str("customer")),
                    ),
                    id("last")
                ),
                id("one"), id("two")
            )

    @Test fun indexAccessNestedTwo() = "roles[ROLES_LIST[0]['customer']" test listOf(
        id("roles"),
        indexAccess(
            id("ROLES_LIST"),
            indexAccess(int(0)),
            indexAccess(str("customer")),
        ),
    )

    @Test fun indexAccessWithConstStringDoubleQuotes() = "start[\"1234\"]" test
            listOf(id("start"), indexAccess(str("1234")))

    @Test fun indexAccessWithConstStringSingleQuotes() = "start['collectAttrs']" test
            listOf(id("start"), indexAccess(str("collectAttrs")))

    @Test fun indexAccessWithConstBoolean() = "start[true]" test
            listOf(id("start"), indexAccess(TokenBoolean(true)))

    @Test fun indexAccessWithConstInt() = "start[123]" test
            listOf(id("start"), indexAccess(int(123)))

    @Test fun indexAccessWithConstFloat() = "start[123.456]" test
            listOf(id("start"), indexAccess(float(123.456)))

    @Test fun indexAccessWithThis() = "start[this]" test
            listOf(id("start"), indexAccess(TokenThis))

    @Test fun indexAccessWithNull() = "start[null]" test
            listOf(id("start"), indexAccess(TokenNull))


    /* === MULTI-VALUE */

    @Test fun multiValue() = "start['aaa', 'bbb']" test
            listOf(id("start"), indexAccess(TokenNull))


}