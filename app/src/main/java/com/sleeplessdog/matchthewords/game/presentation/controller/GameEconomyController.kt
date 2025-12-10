package com.sleeplessdog.matchthewords.game.presentation.controller

import com.sleeplessdog.matchthewords.game.presentation.models.SessionStats

class GameEconomyController(
    private val maxLives: Int,
    private val startLives: Int
) {

    var score: Int = 0
        private set

    var lives: Int = startLives
        private set

    var difficultLevel: Int = 1
        private set

    private val correctIds = linkedSetOf<Int>()
    private val wrongIds = linkedSetOf<Int>()

    fun setup(difficultLevel: Int, lives: Int) {
        this.difficultLevel = difficultLevel
        this.lives = lives
        score = 0
        correctIds.clear()
        wrongIds.clear()
    }

    fun onCorrect(price: Int) {
        score += price
        if (lives < maxLives) lives++
    }

    fun onWrong(penalty: Int): Boolean {
        lives--
        score -= penalty
        return lives <= 0
    }

    fun addCorrectIds(ids: Collection<Int>) {
        correctIds.addAll(ids)
    }

    fun addWrongIds(ids: Collection<Int>) {
        wrongIds.addAll(ids)
    }

    fun buildSessionStats(): SessionStats =
        SessionStats(
            correctIds = correctIds.toList(),
            mistakeIds = wrongIds.toList()
        )

    fun resetScoreOnly() {
        score = 0
        correctIds.clear()
        wrongIds.clear()
    }

    fun resetAll(diffLevelUpdate: Int, livesUpdate: Int) {
        difficultLevel = diffLevelUpdate
        lives = livesUpdate
        score = 0
        correctIds.clear()
        wrongIds.clear()
    }
}
