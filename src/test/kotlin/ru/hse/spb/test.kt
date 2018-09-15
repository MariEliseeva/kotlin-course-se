package ru.hse.spb

import org.junit.Assert.*
import org.junit.Test

class TestSource {
    @Test
    fun testIsInLanguageOneWord() {
        assertTrue(isInLanguage("etr"))
        assertTrue(isInLanguage("feinites"))
        assertTrue(isInLanguage("taliala"))
        assertFalse(isInLanguage("wrongword"))
    }

    @Test
    fun testIsInLanguageNoNoun() {
        assertFalse(isInLanguage("etis atis animatis etis atis amatis"))
        assertFalse(isInLanguage("nataliala kataliala"))
        assertFalse(isInLanguage("feinites inites"))
    }

    @Test
    fun testIsInLanguageBadWord() {
        assertFalse(isInLanguage("nataliala kataliala vetra feinite"))
        assertFalse(isInLanguage("nataliala katalial vetra"))
    }

    @Test
    fun testIsInLanguageBadGender() {
        assertFalse(isInLanguage("natalios kataliala vetra"))
        assertFalse(isInLanguage("nataliala vetr"))
    }

    @Test
    fun testIsInLanguageManySentences() {
        assertFalse(isInLanguage("nataliala vetra feinites vetra"))
        assertFalse(isInLanguage("nataliala kataliala vetra feinites inites tes"))
    }

    @Test
    fun testIsInLanguageOneCorrectSentence() {
        assertTrue(isInLanguage("nataliala kataliala vetra feinites"))
        assertTrue(isInLanguage("petr feinitis"))
        assertTrue(isInLanguage("nataliala kataliala vetra"))
    }
}