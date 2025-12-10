package com.sleeplessdog.matchthewords.game.presentation.models

data class WriteTheWordUi(
    val prompt: String = "",
    val target: String = "",
    val input: String = "",
    val letters: List<WriteTheWordLetterUi> = emptyList(),
    val locked: Boolean = false
)
