package ru.hse.spb

import java.io.OutputStream
import kotlin.streams.toList

interface Element {
    fun render(builder: StringBuilder)
}

class TextElement(private val text: String) : Element {
    override fun render(builder: StringBuilder) {
        builder.append("$text\n")
    }
}

@DslMarker
annotation class TexCommandMarker

@TexCommandMarker
abstract class Command(val name: String, private val options : List<String> =  listOf(),
                       private val arguments : List<String> = listOf()) : Element {
    val children = arrayListOf<Element>()

    protected fun <T : Element> initCommand(command: T, init: T.() -> Unit): T {
        command.init()
        children.add(command)
        return command
    }

    override fun toString(): String {
        val builder = StringBuilder()
        render(builder)
        return builder.toString()
    }

    fun toOutputStream(outputStream: OutputStream){
        val builder = StringBuilder()
        render(builder)
        outputStream.write(builder.toString().toByteArray())
    }

    fun renderOptions() : String {
        if (options.isEmpty()) {
            return ""
        }
        return options.joinToString(",", "[", "]")
    }

    fun renderArguments() : String {
        if (arguments.isEmpty()) {
            return ""
        }
        return arguments.joinToString("}{", "{", "}")
    }

    operator fun String.unaryPlus() {
        children.add(TextElement(this))
    }

    fun flushLeft(init: FlushLeft.() -> Unit) {
        initCommand(FlushLeft(), init)
    }
    fun flushRight(init: FlushRight.() -> Unit) {
        initCommand(FlushRight(), init)
    }
    fun center(init: Center.() -> Unit) {
        initCommand(Center(), init)
    }
}

class FlushLeft: BeginEndCommand("flushleft")
class FlushRight: BeginEndCommand("flushright")
class Center: BeginEndCommand("center")

class Math: Command("") {
    override fun render(builder: StringBuilder) {
        builder.append("$$\n")
        for (c in children) {
            c.render(builder)
        }
        builder.append("$$\n")
    }
}

abstract class LineCommand(name: String, options : List<String> =  listOf(), arguments : List<String> = listOf())
    : Command(name, options, arguments) {

    override fun render(builder: StringBuilder) {
        builder.append("\\$name" + renderArguments() + renderOptions() +'\n')
        for (c in children) {
            c.render(builder)
        }
    }
}


abstract class BeginEndCommand(name: String, options : List<String> = listOf(), arguments : List<String> = listOf())
    : Command(name ,options, arguments) {

    override fun render(builder: StringBuilder) {
        builder.append("\\begin{$name}" +
                renderArguments() + renderOptions() + '\n')
        for (c in children) {
            c.render(builder)
        }
        builder.append("\\end{$name}\n")
    }

    fun itemize(init: Itemize.() -> Unit) {
        initCommand(Itemize(), init)
    }

    fun enumerate(init: Enumerate.() -> Unit) {
        initCommand(Enumerate(), init)
    }

    fun customTag(name: String, vararg options: Pair<String, String>, init: CustomTag.() -> Unit) {
        initCommand(CustomTag(name, options.toList().stream().map { t -> t.first + "=" + t.second }.toList()), init)
    }

    fun math(init: Math.() -> Unit) {
        initCommand(Math(), init)
    }
}

class Itemize : BeginEndCommand("itemize") {
    fun item(init: Item.() -> Unit) {
        initCommand(Item(), init)
    }
}

class Enumerate : BeginEndCommand("enumerate") {
    fun item(init: Item.() -> Unit) {
        initCommand(Item(), init)
    }
}

class Item: LineCommand("item") {
    fun math(init: Math.() -> Unit) {
        initCommand(Math(), init)
    }
}

class CustomTag(name: String, options: List<String>): BeginEndCommand(name, options)

class Document: BeginEndCommand("document") {
    private var documentClass : DocumentClass? = null
    private var usePackages : MutableList<UsePackage> = mutableListOf()

    override fun render(builder: StringBuilder) {
        if (documentClass == null) {
            throw TexBuilderException()
        }
        documentClass!!.render(builder)
        for (usePackage in usePackages) {
            usePackage.render(builder)
        }
        super.render(builder)
    }

    fun documentClass(name: String, vararg  options: String) {
        if (documentClass != null) {
            throw TexBuilderException()
        }
        documentClass = DocumentClass(name, options.toList())
    }

    fun usepackage(name: String, vararg options : String) {
        usePackages.add(UsePackage(name, options.toList()))
    }

    fun frame(frameTitle: String, vararg options: Pair<String, String>, init: Frame.() -> Unit) {
        initCommand(Frame(frameTitle, options.toList().stream().map { t -> t.first + "=" + t.second }.toList()), init)
    }
}

class Frame(name : String, options : List<String>) : BeginEndCommand("frame", options, listOf(name))

class DocumentClass(name : String, options : List<String>) : LineCommand("documentclass", options, listOf(name))

class UsePackage(name : String, options : List<String>): LineCommand("usepackage", options, listOf(name))

fun document(init: Document.() -> Unit) : Document {
    val document = Document()
    document.init()
    return document
}