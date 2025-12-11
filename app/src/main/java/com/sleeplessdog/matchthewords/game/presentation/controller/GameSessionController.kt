package com.sleeplessdog.matchthewords.game.presentation.controller


import com.sleeplessdog.matchthewords.game.presentation.models.GameSettings
import com.sleeplessdog.matchthewords.game.presentation.models.GameType
import com.sleeplessdog.matchthewords.game.presentation.models.StatsState
import com.sleeplessdog.matchthewords.utils.ConstantsApp.ZERO_STRING
import com.sleeplessdog.matchthewords.utils.ConstantsGamePrices
import com.sleeplessdog.matchthewords.utils.SupportFunctions

class GameSessionController(
    private val economy: GameEconomyController,
    private val progress: GameProgressManager
) {

    fun getStatsState(gameType: GameType, todaysScore: String = ZERO_STRING): StatsState {
        return StatsState(
            lives = economy.lives,
            score = economy.score.toString(),
            todaysScore = todaysScore,
            progressSegments = progress.progressSegments,
            progress = progress.progress(gameType)
        )
    }

    fun setup(settings: GameSettings, gameType: GameType) {
        economy.setup(
            difficultLevel = SupportFunctions.getGameDifficult(settings.difficult),
            lives = SupportFunctions.getLivesCount(settings.difficult)
        )
        progress.setup(settings.difficult, gameType)
    }

    fun onCorrect(ids: List<Int>) {
        economy.addCorrectIds(ids)
        economy.onCorrect(ConstantsGamePrices.ANSWER_PRICE)
        progress.onCorrect()
    }

    fun onWrong(ids: List<Int>, gameType: GameType): Boolean {
        economy.addWrongIds(ids)
        val isDead = economy.onWrong(ConstantsGamePrices.MISTAKE_PRICE)
        progress.onWrong(gameType)
        return isDead
    }

    fun resetStats() {
        economy.resetScoreOnly()
        progress.resetOnlyStep()
    }
}
