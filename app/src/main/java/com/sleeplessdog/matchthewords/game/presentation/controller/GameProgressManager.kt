package com.sleeplessdog.matchthewords.game.presentation.controller

import com.sleeplessdog.matchthewords.game.presentation.models.DifficultLevel
import com.sleeplessdog.matchthewords.game.presentation.models.GameType
import com.sleeplessdog.matchthewords.game.presentation.parentControllers.ProgressController

class GameProgressManager(
    private val progressController: ProgressController
) {

    var progressSegments: Int = 1
        private set

    private var currentStep: Int = 0

    fun setup(difficult: DifficultLevel, gameType: GameType) {
        progressSegments = progressController.stepsFor(difficult, gameType)
        currentStep = 0
    }

    fun onCorrect() {
        currentStep++
    }

    fun onWrong(gameType: GameType) {
        if (progressController.advancesOnWrong(gameType)) {
            currentStep++
        }
    }

    fun progress(gameType: GameType): Float {
        return progressController.progressOf(
            currentStep,
            progressSegments,
            gameType
        )
    }

    fun resetOnlyStep() {
        currentStep = 0
    }

    fun reset(
        difficult: DifficultLevel,
        gameType: GameType
    ) {
        setup(difficult, gameType)
    }
}
