package com.sleeplessdog.matchthewords.game.presentation.fragments

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sleeplessdog.matchthewords.game.presentation.models.AnswerEvent
import com.sleeplessdog.matchthewords.game.presentation.models.TfQuestion
import com.sleeplessdog.matchthewords.game.presentation.models.TfQuestionUi
import com.sleeplessdog.matchthewords.game.presentation.models.Word
import com.sleeplessdog.matchthewords.utils.ShuffleFunctions
import com.sleeplessdog.matchthewords.utils.TimeReactionConstants
import kotlin.collections.getOrNull

class TrueOrFalseViewModel (
    private val shuffleFunctions: ShuffleFunctions
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private var questions: List<TfQuestionUi> = emptyList()
    private var index = 0

    private val _ui = MutableLiveData(TfQuestion(word = "", translation = "", locked = true))
    val ui: LiveData<TfQuestion> = _ui

    private val _events = MutableLiveData<AnswerEvent>()
    val events: LiveData<AnswerEvent> = _events

    private val _completed = MutableLiveData(false)
    val completed: LiveData<Boolean> = _completed

    fun setPool(pairs: List<Pair<Word, Word>>) {
        // готовим набор 1-разовых вопросов
        questions = shuffleFunctions.buildTrueFalseSetOnce(pairs, shuffleQuestionsOrder = true)
        index = 0
        nextQuestion()
    }

    fun onTrueClicked() = answer(true)
    fun onFalseClicked() = answer(false)

    private fun answer(userThinksTrue: Boolean) {
        val q = questions.getOrNull(index - 1) ?: return
        val ok = userThinksTrue == q.isCorrect
        _events.value = if (ok) AnswerEvent.CORRECT else AnswerEvent.WRONG
        _ui.value = _ui.value?.copy(locked = true)
        handler.postDelayed({ nextQuestion() }, TimeReactionConstants.NEXT_QUESTION)
    }

    private fun nextQuestion() {
        if (index >= questions.size) { _completed.value = true; return }
        val q = questions[index++]
        _ui.value = TfQuestion(
            word = q.word.text,
            translation = q.shownTranslation.text,
            locked = false
        )
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(null)
        super.onCleared()
    }
}