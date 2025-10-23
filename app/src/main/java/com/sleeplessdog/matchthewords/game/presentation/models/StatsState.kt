package com.sleeplessdog.matchthewords.game.presentation.models

data class StatsState(
    var lives: Int = 3,
    var score: String = "0",
    var todaysScore: String = "0",
    var answerPointsState: AnswerPointsState = AnswerPointsState.EMPTY,
    val progressSegments : Int = 4,
    var progress: Float = 0.0f
)
