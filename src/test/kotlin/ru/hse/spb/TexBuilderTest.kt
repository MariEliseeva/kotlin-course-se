package ru.hse.spb

import org.junit.Assert.*
import org.junit.Test

class TexBuilderTest {
    @Test
    fun testEmptyDocument() {
        assertEquals("\\documentclass{article}\n" +
                "\\begin{document}\n" +
                "\\end{document}\n",
            document { documentClass("article") }.toString())
    }

    @Test
    fun testDocumentWithPackages() {
        assertEquals("\\documentclass{article}[12pt]\n" +
                "\\usepackage{graphicx}\n" +
                "\\begin{document}\n" +
                "\\end{document}\n",
            document {
                documentClass("article", "12pt")
                usepackage("graphicx")
            }.toString())
    }

    @Test(expected = TexBuilderException::class)
    fun testDocumentWithoutDocumentClass() {
       document {
       }.toString()
    }

    @Test(expected = TexBuilderException::class)
    fun testDocumentWithTwoDocumentClasses() {
        document {
            documentClass("class1")
            documentClass("class2")
        }.toString()
    }

    @Test
    fun testText() {
        assertEquals("\\documentclass{article}\n" +
                "\\begin{document}\n" +
                "sometext\n" +
                "\\end{document}\n",
            document {
                documentClass("article")
                + "sometext"
            }.toString())
    }

    @Test
    fun testFrame() {
        assertEquals("\\documentclass{article}\n" +
                "\\begin{document}\n" +
                "\\begin{frame}{title}[arg1=arg2]\n" +
                "TEXT\n" +
                "\\end{frame}\n" +
                "\\end{document}\n",
            document {
                documentClass("article")
                frame("title", "arg1" to "arg2") {
                    +"TEXT"
                }
            }.toString())
    }

    @Test
    fun testItemize() {
        assertEquals("\\documentclass{article}\n" +
                "\\begin{document}\n" +
                "\\begin{frame}{title}[arg1=arg2]\n" +
                "\\begin{itemize}\n" +
                "\\item\nITEM1\n" +
                "\\item\nITEM2\n" +
                "\\end{itemize}\n" +
                "\\end{frame}\n" +
                "\\end{document}\n",
            document {
                documentClass("article")
                frame("title", "arg1" to "arg2") {
                    itemize {
                        item {
                            +"ITEM1"
                        }
                        item {
                            +"ITEM2"
                        }

                    }
                }
            }.toString())
    }

    @Test
    fun testEnumerate() {
        assertEquals("\\documentclass{article}\n" +
                "\\begin{document}\n" +
                "\\begin{enumerate}\n" +
                "\\item\nITEM\n" +
                "\\end{enumerate}\n" +
                "\\end{document}\n",
            document {
                documentClass("article")
                enumerate {
                    item {
                        +"ITEM"
                    }
                }
            }.toString())
    }

    @Test
    fun testMath() {
        assertEquals("\\documentclass{article}\n" +
                "\\begin{document}\n" +
                "$$\n" +
                "2+2=5\n" +
                "$$\n" +
                "\\end{document}\n",
            document {
                documentClass("article")
                math {
                    +"2+2=5"
                }
            }.toString())
    }

    @Test
    fun testAlignment() {
        assertEquals("\\documentclass{article}\n" +
                "\\begin{document}\n" +
                "\\begin{flushright}\n" +
                "THETEXT\n" +
                "\\end{flushright}\n" +
                "\\end{document}\n",
            document {
                documentClass("article")
                flushRight {
                    +"THETEXT"
                }
            }.toString())
    }

    @Test
    fun testCustomTag() {
        assertEquals("\\documentclass{article}\n" +
                "\\begin{document}\n" +
                "\\begin{sometag}[option1=17]\n" +
                "tagtext\n" +
                "\\end{sometag}\n" +
                "\\end{document}\n",
            document {
                documentClass("article")
                customTag("sometag", "option1" to "17") {
                    +"tagtext"
                }
            }.toString())
    }

    @Test
    fun testExample() {
        val rows : List<String> = listOf("row1", "row2", "row3")
        assertEquals("\\documentclass{beamer}\n" +
                "\\usepackage{babel}[russian]\n" +
                "\\begin{document}\n" +
                "\\begin{frame}{frametitle}[arg1=arg2]\n" +
                "\\begin{itemize}\n" +
                "\\item\nrow1 text\n" +
                "\\item\nrow2 text\n" +
                "\\item\nrow3 text\n" +
                "\\end{itemize}\n" +
                "\\begin{pyglist}[language=kotlin]\n" +
                "\n" +
                "                    |val a = 1\n" +
                "                    |\n" +
                "                \n" +
                "\\end{pyglist}\n" +
                "\\end{frame}\n" +
                "\\end{document}\n",
            document {
                documentClass("beamer")
                usepackage("babel", "russian")
                frame("frametitle", "arg1" to "arg2") {
                    itemize {
                        for (row in rows) {
                            item { + "$row text" }
                        }
                    }

                    customTag("pyglist", "language" to "kotlin") {
                        +"""
                    |val a = 1
                    |
                """
                    }
                }
            }.toString())
    }

    @Test
    fun bigTest() {
        assertEquals("\\documentclass{article}[12pt]\n" +
                "\\usepackage{graphicx}\n" +
                "\\usepackage{fontenc}[T1]\n" +
                "\\usepackage{verbments}\n" +
                "\\usepackage{inputenc}[utf8]\n" +
                "\\begin{document}\n" +
                "\\begin{frame}{title}[arg1=arg2]\n" +
                "\\begin{itemize}\n" +
                "\\item\n" +
                "item1\n" +
                "\\item\n" +
                "\$\$\n" +
                "a/b\n" +
                "\$\$\n" +
                "\\end{itemize}\n" +
                "sss\n" +
                "\\end{frame}\n" +
                "\\begin{flushright}\n" +
                "aaa\n" +
                "\$\$\n" +
                "1+4\n" +
                "\$\$\n" +
                "\\begin{enumerate}\n" +
                "\\item\n" +
                "123\n" +
                "\\begin{itemize}\n" +
                "\\item\n" +
                "1\n" +
                "\\item\n" +
                "\\item\n" +
                "\$\$\n" +
                "123\n" +
                "\$\$\n" +
                "\\end{itemize}\n" +
                "\\end{enumerate}\n" +
                "\\end{flushright}\n" +
                "\\begin{pyglist}[language=kotlin]\n" +
                "\n" +
                "               |val a = 1\n" +
                "               |\n" +
                "            \n" +
                "\\end{pyglist}\n" +
                "\\end{document}\n",
            document {
                documentClass ("article", "12pt")
                usepackage("graphicx")
                usepackage("fontenc", "T1")
                usepackage("verbments")
                usepackage("inputenc", "utf8")
                frame("title", "arg1" to "arg2") {
                    itemize {
                        item{ + "item1"}
                        item{ math { +"a/b" }}
                    }
                    +"sss"
                }
                flushRight {
                    +"aaa"
                    math {
                        +"1+4"
                    }
                    enumerate {
                        item { +"123" }
                        itemize {
                            item { +"1" }
                            item { }
                            item {
                                math { +"123"

                                }
                            }
                        }
                    }
                }
                customTag("pyglist", "language" to "kotlin") {
                    +"""
               |val a = 1
               |
            """
                }
            }.toString())
    }
}