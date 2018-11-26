package ru.hse.spb

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.spb.hse.parser.ExpLexer
import ru.spb.hse.parser.ExpParser

class ParsingTest {

    private fun testParser(stringToParse : String, expectedParsedTree : String) {
        val expLexer = ExpLexer(CharStreams.fromString(stringToParse))
        val parser = ExpParser(BufferedTokenStream(expLexer))
        assertEquals("(file (block ($expectedParsedTree)) <EOF>)".replace(" ", ""),
            parser.file().toStringTree(parser).replace(" ", ""))
    }

    @Test
    fun testAssignment() {
        testParser("var x = 3", "statement (variable var x = (expression 3))")
    }

    @Test
    fun testFunction() {
        testParser("fun foo (c) { println(c)}",
            "statement (function fun foo ( (parameterNames c) ) " +
                    "{ (block (" +
                    "statement (expression (functionCall println ( (arguments (expression c)) )))" +
                    ")) })")
    }

    @Test
    fun testIf() {
        testParser("if (x == 3) {x = 2} else {x = 1}",
            "statement (ifStatement if ( (expression (expression x) == (expression 3)) ) " +
                    "{ (block (" +
                    "statement (assignment x = (expression 2))" +
                    ")) } else { (block (" +
                    "statement (assignment x = (expression 1))" +
                    ")) })")
    }
}