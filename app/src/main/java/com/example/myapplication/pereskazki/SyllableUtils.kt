package com.example.utils

private val VOWELS = setOf('а', 'е', 'є', 'и', 'і', 'ї', 'о', 'у', 'ю', 'я', 'ы', 'э', 'ё')

private fun isVowel(char: Char): Boolean {
    return char.lowercaseChar() in VOWELS
}

private fun countVowels(word: String): Int {
    return word.count { isVowel(it) }
}

fun splitIntoSyllables(word: String): List<String> {
    val vowelsCount = countVowels(word)
    if (vowelsCount <= 1) {
        return listOf(word)
    }

    val syllables = mutableListOf<String>()
    var syllable = ""
    var prevCharIsVowel = false
    var prevCharIsConsonant = false
    var prevPrevCharIsConsonant = false
    var i = 0
    while (i < word.length) {
        val char = word[i]
        if (isVowel(char)) {
            if (syllable.isNotEmpty() && !prevCharIsVowel && !prevPrevCharIsConsonant) {
                syllables.add(syllable)
                syllable = ""
            }
            prevCharIsVowel = true
            prevPrevCharIsConsonant = prevCharIsConsonant
            prevCharIsConsonant = false
            syllable += char
            i++
        } else {
            prevPrevCharIsConsonant = prevCharIsConsonant
            prevCharIsConsonant = true
            val nextCharIsVowel = (i < word.length - 1) && isVowel(word[i + 1])
            if (syllable.isEmpty()) {
                syllable += char
                i++
            } else if (nextCharIsVowel && !prevPrevCharIsConsonant) {
                syllable += char
                i++
            } else if (prevCharIsConsonant && !prevPrevCharIsConsonant) {
                syllable += char
                i++
            } else {
                syllables.add(syllable)
                syllable = ""
            }
        }
    }
    if (syllable.isNotEmpty()) {
        syllables.add(syllable)
    }

    val resultSyllables = mutableListOf<String>()
    var idx = 0
    while (idx < syllables.size) {
        var syllable = syllables[idx]
        if (syllable.length > 2 && syllable.endsWith("ь") && !syllable.endsWith("ів")) {
            val nextSyllable = if (idx < syllables.size - 1) syllables[idx + 1] else null
            if (nextSyllable != null && !isVowel(nextSyllable[0])) {
                syllable += nextSyllable[0]
                idx++
            }
        }
        resultSyllables.add(syllable)
        idx++
    }

    return resultSyllables
}

