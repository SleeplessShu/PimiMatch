package com.sleeplessdog.matchthewords.game.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleeplessdog.matchthewords.game.data.repositories.AppPrefs
import com.sleeplessdog.matchthewords.game.data.repositories.LanguagePrefs
import com.sleeplessdog.matchthewords.game.domain.api.ScoreInteractor
import com.sleeplessdog.matchthewords.game.domain.interactors.WordsController
import com.sleeplessdog.matchthewords.game.domain.models.WordsCategoriesList
import com.sleeplessdog.matchthewords.game.domain.usecase.GetSelectedCategoriesUC
import com.sleeplessdog.matchthewords.game.presentation.controller.GameEconomyController
import com.sleeplessdog.matchthewords.game.presentation.controller.GameProgressManager
import com.sleeplessdog.matchthewords.game.presentation.interfaces.GameEvent
import com.sleeplessdog.matchthewords.game.presentation.models.DifficultLevel
import com.sleeplessdog.matchthewords.game.presentation.models.GameSettings
import com.sleeplessdog.matchthewords.game.presentation.models.GameState
import com.sleeplessdog.matchthewords.game.presentation.models.GameType
import com.sleeplessdog.matchthewords.game.presentation.models.MatchState
import com.sleeplessdog.matchthewords.game.presentation.models.StatsState
import com.sleeplessdog.matchthewords.game.presentation.models.Word
import com.sleeplessdog.matchthewords.game.presentation.parentControllers.ProgressController
import com.sleeplessdog.matchthewords.utils.ConstantsApp.MAX_LIVES
import com.sleeplessdog.matchthewords.utils.ConstantsApp.START_LIVES
import com.sleeplessdog.matchthewords.utils.ConstantsApp.ZERO_STRING
import com.sleeplessdog.matchthewords.utils.ConstantsGamePrices
import com.sleeplessdog.matchthewords.utils.ConstantsGamePrices.ONE_OF_FOUR_MULTIPLIER
import com.sleeplessdog.matchthewords.utils.ConstantsGamePrices.WRITE_WORD_DIVIDER_LIST
import com.sleeplessdog.matchthewords.utils.ConstantsTimeReaction
import com.sleeplessdog.matchthewords.utils.SupportFunctions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel(
    private val wordsController: WordsController,
    progressController: ProgressController,
    private val scoreInteractor: ScoreInteractor,
    private val appPrefs: AppPrefs,
    private val languagePrefs: LanguagePrefs,
    private val getSelectedCategoriesUC: GetSelectedCategoriesUC
) : ViewModel() {

    private val _gameState = MutableLiveData(MatchState())
    val gameState: LiveData<MatchState> = _gameState

    private val _statsState = MutableLiveData(StatsState())
    val statsState: LiveData<StatsState> = _statsState

    private val _gameSettings = MutableLiveData(GameSettings())
    val gameSettings: LiveData<GameSettings> = _gameSettings

    private val _wordsPairs = MutableLiveData<List<Pair<Word, Word>>>()
    val wordsPairs: LiveData<List<Pair<Word, Word>>> = _wordsPairs

    private val _showExitDialogEvent = MutableLiveData<Unit>()
    val showExitDialogEvent: LiveData<Unit> = _showExitDialogEvent

    private val economy = GameEconomyController(
        maxLives = MAX_LIVES,
        startLives = START_LIVES
    )

    private val progressManager = GameProgressManager(progressController)

    private var allPairs: List<Pair<Word, Word>> = emptyList()

    init {
        onGame()
    }

    // ========================= GAME FLOW =========================

    fun setGame(gameType: GameType) {
        _gameState.value = _gameState.value?.copy(gameType = gameType)
    }

    fun onGame() {
        viewModelScope.launch {
            prepareData()
            setupGameStats()

            val ok = loadWordsFromDatabase()
            if (!ok) return@launch

            _wordsPairs.value = allPairs
            delay(ConstantsTimeReaction.LOADING)
            _gameState.value = _gameState.value?.copy(state = GameState.GAME)
        }
    }

    // ========================= EVENTS =========================

    fun onGameEvent(ev: GameEvent) {
        when (ev) {
            is GameEvent.Correct -> {
                economy.addCorrectIds(ev.wordsIds)
                reactOnCorrect()
            }

            is GameEvent.Wrong -> {
                economy.addWrongIds(ev.wordsIds)
                reactOnError()
            }

            GameEvent.Completed -> onGameEnd()
        }
    }

    private fun reactOnCorrect() {
        economy.onCorrect(ConstantsGamePrices.ANSWER_PRICE)
        progressManager.onCorrect()
        emitStats()
    }

    private fun reactOnError() {
        val isDead = economy.onWrong(ConstantsGamePrices.MISTAKE_PRICE)

        val type = _gameState.value?.gameType ?: GameType.MATCH8
        progressManager.onWrong(type)

        emitStats()

        if (isDead) onGameEnd()
    }

    // ========================= DATA =========================

    private suspend fun prepareData() {
        setLoading()

        val selectedCategories = getSelectedCategoriesUC()

        val enums = selectedCategories.mapNotNull { cat ->
            WordsCategoriesList.entries.find { it.key == cat.key }
        }.toSet()

        _gameSettings.value = GameSettings(
            language1 = languagePrefs.getUiLanguage(),
            language2 = languagePrefs.getStudyLanguage(),
            difficult = appPrefs.getDifficulty(),
            level = appPrefs.getLevels(),
            category = enums
        )
    }

    private suspend fun loadWordsFromDatabase(): Boolean {
        val gameType = _gameState.value?.gameType ?: GameType.MATCH8

        val wordsNeeded = when (gameType) {
            GameType.WriteTheWord -> economy.difficultLevel / WRITE_WORD_DIVIDER_LIST
            GameType.OneOfFour   -> economy.difficultLevel * ONE_OF_FOUR_MULTIPLIER
            else                 -> economy.difficultLevel
        }

        val settings = _gameSettings.value ?: return false

        val pairs = wordsController.getWordPairs(
            settings.language1,
            settings.language2,
            settings.level,
            wordsNeeded,
            settings.category
        )

        if (pairs.isEmpty()) {
            onGameEnd()
            return false
        }

        allPairs = pairs
        return true
    }

    // ========================= GAME END =========================

    fun onGameEnd() {
        setLoading()

        val stats = economy.buildSessionStats()
        val todaysScore = scoreInteractor.getTodaysResult()

        viewModelScope.launch {
            delay(ConstantsTimeReaction.LOADING)

            _gameState.value = _gameState.value?.copy(state = GameState.END_OF_GAME)

            _statsState.value = _statsState.value?.copy(
                lives = economy.lives,
                todaysScore = todaysScore.toString()
            )

            wordsController.putRoundStats(stats)
            scoreInteractor.updateTodaysResult(economy.score)
        }
    }

    // ========================= RESET =========================

    fun restartGame() {
        resetStats()
        onGame()
    }

    fun resetStats() {
        economy.resetScoreOnly()
        _wordsPairs.value = emptyList()
        progressManager.resetOnlyStep()
        emitStats()
    }

    fun resetAll() {
        val diff = DifficultLevel.MEDIUM

        economy.resetAll(
            diffLevelUpdate = SupportFunctions.getGameDifficult(diff),
            livesUpdate = SupportFunctions.getLivesCount(diff)
        )

        _gameSettings.value = GameSettings()
        _gameState.value = MatchState(state = GameState.GAME)

        progressManager.reset(diff, GameType.MATCH8)
        emitStats()
    }

    // ========================= STATS =========================

    private fun setupGameStats() {
        val difficult = _gameSettings.value?.difficult ?: DifficultLevel.MEDIUM

        economy.setup(
            difficultLevel = SupportFunctions.getGameDifficult(difficult),
            lives = SupportFunctions.getLivesCount(difficult)
        )

        progressManager.setup(
            difficult = difficult,
            gameType = _gameState.value?.gameType ?: GameType.MATCH8
        )

        emitStats()
    }

    private fun emitStats() {
        val type = _gameState.value?.gameType ?: GameType.MATCH8

        _statsState.value = _statsState.value?.copy(
            lives = economy.lives,
            score = economy.score.toString(),
            progressSegments = progressManager.progressSegments,
            progress = progressManager.progress(type)
        ) ?: StatsState(
            lives = economy.lives,
            score = economy.score.toString(),
            todaysScore = ZERO_STRING,
            progressSegments = progressManager.progressSegments,
            progress = progressManager.progress(type)
        )
    }

    fun showGameExitQuestion() {
        _showExitDialogEvent.value = Unit
    }

    private fun setLoading() {
        _gameState.value = _gameState.value?.copy(state = GameState.LOADING)
    }
}
