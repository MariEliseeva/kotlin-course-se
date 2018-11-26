package ru.hse.spb

import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.ByteArrayOutputStream
import java.io.PrintStream


class InterpreterTest {
    @Rule @JvmField
    var testFolder = TemporaryFolder()

    private fun runTest(result : String, input : String) {
        val file = testFolder.newFile()
        file.writeText(input)
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))
        runCode(file.absolutePath)
        assertEquals(result, outputStream.toString())
    }

    @Test
    fun testSimple() {
        runTest("3\n", "println(1+2)")
    }

    @Test
    fun testFunctionCall() {
        runTest("5\n", "fun foo(x, y) { return x + y } println(foo(2, 3))")
    }

    @Test
    fun testIfStatement() {
        runTest("17\n", "if (2 + 3 == 4) {println(11)} else {println(17)}")
    }

    @Test
    fun testAssignment() {
        runTest("5\n", "var x = 4 x = x + 1 println(x)")
    }

    @Test
    fun testWhile() {
        runTest("13\n", "var x = 1 while (x < 10) {x = x + 4} println(x)")
    }

    @Test
    fun testBar() {
        runTest("42\n", "fun foo(n) {\n" +
                "    fun bar(m) {\n" +
                "        return m + n\n" +
                "    }\n" +
                "\n" +
                "    return bar(1)\n" +
                "}\n" +
                "\n" +
                "println(foo(41)) ")
    }

    @Test
    fun testFib() {
        runTest("8\n", "fun fib(x) {if (x <= 1) {return 1}" +
                " return fib(x - 1) + fib(x - 2)} println(fib(5))")
    }
}