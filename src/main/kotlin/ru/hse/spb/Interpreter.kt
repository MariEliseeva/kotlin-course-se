package ru.hse.spb

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import ru.spb.hse.parser.ExpBaseVisitor
import ru.spb.hse.parser.ExpLexer
import ru.spb.hse.parser.ExpParser

fun runCode(fileName: String) {
    val expLexer = ExpLexer(CharStreams.fromFileName(fileName))
    val parser = ExpParser(BufferedTokenStream(expLexer))
    Visitor().visitFile(parser.file())
}

class Visitor: ExpBaseVisitor<Int?>() {
    private var varScope = Scope<Int?>()
    private var funScope = Scope<ExpParser.FunctionContext?>()

    override fun visitFile(ctx: ExpParser.FileContext): Int? {
        return visit(ctx.block())
    }
    override fun visitBlock(ctx: ExpParser.BlockContext): Int? {
        for (statement in ctx.statement()) {
            val result = visit(statement.children[0])
            if (result != null && statement.expression() == null) {
                return result
            }
        }
        return null
    }

    override fun visitStatement(ctx: ExpParser.StatementContext): Int? {
        return visit(ctx.children[0])
    }

    override fun visitFunction(ctx: ExpParser.FunctionContext): Int? {
        exceptionAware(ctx.start.line) {
            funScope.addItem(ctx.Identifier().text, ctx)
        }
        return null
    }

    override fun visitVariable(ctx: ExpParser.VariableContext): Int? {
        exceptionAware(ctx.start.line) {
            if (ctx.expression() != null) {
                varScope.addItem(ctx.Identifier().text, visit(ctx.expression()))
            } else {
                varScope.addItem(ctx.Identifier().text)
            }
        }
        return null
    }

    override fun visitWhileStatement(ctx: ExpParser.WhileStatementContext): Int? {
        while (visit(ctx.expression()) == 1) {
            val result = visit(ctx.block())
            if (result != null) {
                return result
            }
        }
        return null
    }

    override fun visitIfStatement(ctx: ExpParser.IfStatementContext): Int? {
        if (visit(ctx.expression()) == 1) {
            return visit(ctx.block()[0])
        }
        if (ctx.block().size == 2) {
            return visit(ctx.block()[1])
        }
        return null
    }

    override fun visitAssignment(ctx: ExpParser.AssignmentContext): Int? {
        exceptionAware(ctx.start.line) {
            varScope.setItem(ctx.Identifier().text, visit(ctx.expression()))
        }
        return null
    }

    override fun visitReturnStatement(ctx: ExpParser.ReturnStatementContext): Int? {
        return visit(ctx.expression())
    }

    override fun visitExpression(ctx: ExpParser.ExpressionContext): Int? {
        return when {
            ctx.Literal() != null -> {
                ctx.Literal().text.toInt()
            }
            ctx.Identifier() != null -> {
                try {
                    varScope.getItem(ctx.Identifier().text)
                } catch (e : ScopeException) {
                    throw InterpretationException(ctx.start.line, e.message!!)
                }
            }
            ctx.functionCall() != null -> {
                visit(ctx.functionCall())
            }
            ctx.expression() == null || ctx.expression().size == 1 -> {
                visit(ctx.getChild(0))
            }
            ctx.expression().size == 2 -> {
                val left = visit(ctx.expression(0))!!
                val right = visit(ctx.expression(1))!!
                when (ctx.Operation().text) {
                    "+" -> return left + right
                    "-" -> return left - right
                    "*" -> return left * right
                    "/" -> {
                        if (right == 0) {
                            throw InterpretationException(ctx.start.line, "Division by zero")
                        } else {
                            return left / right
                        }
                    }
                    "%" -> if (right == 0) {
                        throw InterpretationException(ctx.start.line, "Division by zero")
                    } else {
                        return left % right
                    }
                    ">" -> return (left > right).toInt()
                    "<" -> return (left < right).toInt()
                    ">=" -> return (left >= right).toInt()
                    "<=" -> return (left <= right).toInt()
                    "==" -> return (left == right).toInt()
                    "!=" -> return (left != right).toInt()
                    "&&" -> return (left.toBoolean() && right.toBoolean()).toInt()
                    "||" -> return (left.toBoolean() || right.toBoolean()).toInt()
                    else -> throw InterpretationException(ctx.start.line,
                        "Unsupported operation: " + ctx.Operation().text)
                }
            }
            else -> throw InterpretationException(ctx.start.line, "Unsupported expression")
        }
    }

    override fun visitFunctionCall(ctx: ExpParser.FunctionCallContext): Int? {
        if (ctx.Identifier().text == "println") {
            println(ctx.arguments().expression().map { visit(it) }.joinToString("\n"))
            return 0
        }
        try {
            val functionContext = funScope.getItem(ctx.Identifier().text)
            val parameterNames = functionContext!!.parameterNames().children
            varScope = Scope(varScope)
            for (i in 0 until parameterNames.size) {
                varScope.addItem(parameterNames[i].text, visit(ctx.arguments().children[i]))
            }
            val result = visit(functionContext.block())
            varScope = varScope.parent!!
            if (result != null) {
                return result
            }
            return 0
        } catch (e : ScopeException) {
            throw InterpretationException(ctx.start.line, e.message!!)
        }
    }

    private fun Boolean.toInt() = if (this) 1 else 0

    private fun Int.toBoolean() = this != 0

    private inline fun exceptionAware(startLine: Int, f: () -> Unit) {
        try {
            f()
        } catch (e : ScopeException) {
            throw InterpretationException(startLine, e.message!!)
        }
    }
}