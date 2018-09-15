package ru.hse.spb

fun isInLanguage(sentence: String?): Boolean {
    val words = sentence?.split(" ") ?: return false
    if (words.size == 1) {
        return isWord(words[0])
    }
    val isFeminine = isFeminine(words[0])
    var index = 0
    index = indexOfNextPartOfSpeech(index, words, isFeminine, ::isAdjective)
    if (index == -1
        || index == words.size
        || isFeminine(words[index]) != isFeminine
        || !isNoun(words[index])
    ) {
        return false
    }
    index++
    index = indexOfNextPartOfSpeech(index, words, isFeminine, ::isVerb)
    if (index != words.size) {
        return false
    }
    return true
}

private fun indexOfNextPartOfSpeech(
    start: Int, words: List<String>,
    isFeminine: Boolean,
    isPartOfSpeech: (String) -> Boolean
): Int {
    var index = start
    while (index < words.size && isPartOfSpeech(words[index])) {
        if (isFeminine(words[index]) != isFeminine) {
            return -1
        }
        index++
    }
    return index
}

private fun isWord(word: String): Boolean {
    return isAdjective(word) || isVerb(word) || isNoun(word);
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
    if (isInLanguage(readLine())) {
        println("YES")
    } else {
        println("NO")
    }
}