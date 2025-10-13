package com.sleeplessdog.matchthewords.game.presentation.models

data class GameUiOOF(
    val originalText: String = "",
    val options: List<String> = listOf("", "", "", ""),
    val states: List<ButtonState> = List(4) { ButtonState.DEFAULT },
    val locked: Boolean = false
)