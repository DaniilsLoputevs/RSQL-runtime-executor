package org.example.io.github.daniils_loputevs.rsql_runtime_executor.selector

import java.util.*


// TODO - multi value IndexAccess

/**
 * is goes down by stack
 * is goes up by stack
 *
 * /* check constants */
 * is this -> parseConstThis
 * is int OR float -> parseConstNum
 * is str -> parseConstStr
 * is bool -> parseConstBool
 *
 * is identifier -> parseConstSegment
 */
class SelectorParser {
    fun parse(str: String): Expression {
        val stack = LinkedList<Expression>()
        val iter = SelectorParseIterator(str)
        val rootExpr = Expression()
        var currExpr = rootExpr

        while (iter.hasNext()) {
            iter.skipWhitespace()
            val ch = iter.curr

            // "user.info.extra.counter[user.info.last].start[\"1234\"]"
            when {
                ch == '[' -> IndexAccess().let { currExpr += it; stack.addLast(currExpr); currExpr = it; }
                ch == ']' -> run { currExpr = stack.removeLast(); }
                ch == ',' -> run {  }

                iter.isThis() -> iter.parseConstThis().also { currExpr += it }
                iter.isNull() -> iter.parseConstNull().also { currExpr += it }
                ch.isDigit() -> iter.parseConstNumber().also { currExpr += it }
                iter.isBoolean() -> iter.parseConstBoolean().also { currExpr += it }
                iter.isString() -> iter.parseConstString().also { currExpr += it }
                ch.isLetterOrDigit() -> iter.parseIdentifier().also { currExpr += it }
                /* continue to parse Identifier, skip dot in the middle of path */
                ch == '.' -> run { iter.position++; iter.parseIdentifier().also { currExpr += it } }

                else -> throw IllegalArgumentException("Unexpected character(char='${ch}') at position ${iter.position}")
            }
            iter.position++
        }
        return rootExpr
    }
}

