package com.sleeplessdog.matchthewords.game.presentation.models

data class StatsState(
    var lives: Int = 3,
    var score: String = "00000",
    var todaysScore: String = "00000",
    var answerPointsState: AnswerPointsState = AnswerPointsState.EMPTY
)
