package com.sleeplessdog.matchthewords.game.presentation.parentControllers

import com.sleeplessdog.matchthewords.game.presentation.models.DifficultLevel
import com.sleeplessdog.matchthewords.game.presentation.models.GameType
import com.sleeplessdog.matchthewords.utils.SupportFunctions

class ProgressController(private val support: SupportFunctions) {

    fun segmentsFor(difficult: DifficultLevel, type: GameType): Int {
        val wordsInMatch = support.getGameDifficult(difficult)
        return when (type) {
            GameType.MATCH8 -> (wordsInMatch / MATCH8_STEP_SIZE).coerceAtLeast(1)
            GameType.TRUEorFALSE,
            GameType.OneOfFour,
            GameType.WriteTheWord -> wordsInMatch
        }
    }

    fun progressOf(currentStep: Int, segments: Int): Float {
        if (segments <= 0) return 0f
        return (currentStep.toFloat() / segments).coerceIn(0f, 1f)
    }

    fun advancesOnWrong(type: GameType): Boolean =
        type == GameType.TRUEorFALSE

    private companion object {
        private const val MATCH8_STEP_SIZE = 6
    }
}
