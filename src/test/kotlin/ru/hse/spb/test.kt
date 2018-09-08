package ru.hse.spb

import org.junit.Assert.assertEquals
import org.junit.Test

class TestSource {
    @Test
    fun testIsInLanguageOneWord() {
        assertEquals("YES", isInLanguage("etr"))
        assertEquals("YES", isInLanguage("feinites"))
        assertEquals("YES", isInLanguage("taliala"))
        assertEquals("NO", isInLanguage("wrongword"))
    }

    @Test
    fun testIsInLanguageNoNoun() {
        assertEquals("NO", isInLanguage("etis atis animatis etis atis amatis"))
        assertEquals("NO", isInLanguage("nataliala kataliala"))
        assertEquals("NO", isInLanguage("feinites inites"))
    }

    @Test
    fun testIsInLanguageBadWord() {
        assertEquals("NO", isInLanguage("nataliala kataliala vetra feinite"))
        assertEquals("NO", isInLanguage("nataliala katalial vetra"))
    }

    @Test
    fun testIsInLanguageBadGender() {
        assertEquals("NO", isInLanguage("natalios kataliala vetra"))
        assertEquals("NO", isInLanguage("nataliala vetr"))
    }

    @Test
    fun testIsInLanguageManySentences() {
        assertEquals("NO", isInLanguage("nataliala vetra feinites vetra"))
        assertEquals("NO", isInLanguage("nataliala kataliala vetra feinites inites tes"))
    }

    @Test
    fun testIsInLanguageOneCorrectSentence() {
        assertEquals("YES", isInLanguage("nataliala kataliala vetra feinites"))
        assertEquals("YES", isInLanguage("petr feinitis"))
        assertEquals("YES", isInLanguage("nataliala kataliala vetra"))
    }
}