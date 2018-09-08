package ru.hse.spb

fun isInLanguage(sentence: String?): String {
    val wordsArray = sentence?.split(" ") ?: return "NO"
    if (wordsArray.size == 1) {
        return isWord(wordsArray[0])
    }
    val isFeminine = isFeminine(wordsArray[0])
    var index = 0
    while (index < wordsArray.size && isAdjective(wordsArray[index])) {
        if (isFeminine(wordsArray[index]) != isFeminine) {
            return "NO"
        }
        index++
    }
    if (index == wordsArray.size
        || isFeminine(wordsArray[index]) != isFeminine
        || !isNoun(wordsArray[index])
    ) {
        return "NO"
    }
    index++
    while (index < wordsArray.size && isVerb(wordsArray[index])) {
        if (isFeminine(wordsArray[index]) != isFeminine) {
            return "NO"
        }
        index++
    }
    if (index != wordsArray.size) {
        return "NO"
    }
    return "YES"
}

private fun isWord(word: String): String {
    if (isAdjective(word) || isVerb(word) || isNoun(word)) {
        return "YES"
    }
    return "NO"
}

private fun isAdjective(word: String): Boolean {
    return word.endsWith("lios") || word.endsWith("liala")
}

private fun isVerb(word: String): Boolean {
    return word.endsWith("initis") || word.endsWith("inites")
}

private fun isNoun(word: String): Boolean {
    return word.endsWith("etr") || word.endsWith("etra")
}

private fun isFeminine(word: String): Boolean {
    return word.endsWith("liala")
            || word.endsWith("etra")
            || word.endsWith("inites")
}

fun main(args: Array<String>) {
    println(isInLanguage(readLine()))
}