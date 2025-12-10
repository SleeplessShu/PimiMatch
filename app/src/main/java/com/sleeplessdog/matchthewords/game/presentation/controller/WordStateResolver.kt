package com.sleeplessdog.matchthewords.game.presentation.controller

import com.sleeplessdog.matchthewords.game.presentation.models.ButtonState
import com.sleeplessdog.matchthewords.game.presentation.models.Word

class WordStateResolver {
    fun resolve(
        word: Word,
        selected: List<Word>,
        errors: List<Word>,
        used: Set<Int>,
        correct: Set<Int>
    ): ButtonState = when {
        errors.any { it === word } -> ButtonState.ERROR
        word.id in used           -> ButtonState.DISABLED
        word.id in correct        -> ButtonState.CORRECT
        selected.any { it === word } -> ButtonState.SELECTED
        else -> ButtonState.DEFAULT
    }
}
