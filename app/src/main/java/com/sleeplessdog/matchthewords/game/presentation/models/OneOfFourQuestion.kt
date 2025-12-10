package com.sleeplessdog.matchthewords.game.presentation.models

data class OneOfFourQuestion(
    val originalFirst: Word,
    val optionsSecond: List<Word>,
    val correctSecondId: Int,
    val consumedFirstIds: Set<Int>
)
