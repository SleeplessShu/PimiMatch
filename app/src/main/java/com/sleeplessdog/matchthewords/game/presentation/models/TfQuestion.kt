package com.sleeplessdog.matchthewords.game.presentation.models

data class TfQuestion(
    val word: String = "",
    val translation: String = "",
    val locked: Boolean = false
)