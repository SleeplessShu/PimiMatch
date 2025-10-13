package com.sleeplessdog.matchthewords.game.presentation.fragments

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sleeplessdog.matchthewords.game.presentation.models.AnswerEvent
import com.sleeplessdog.matchthewords.game.presentation.models.IngameWordsState
import com.sleeplessdog.matchthewords.game.presentation.models.Word
import com.sleeplessdog.matchthewords.utils.ShuffleFunctions
import com.sleeplessdog.matchthewords.utils.TimeReactionConstants

class WordsMatchingViewModel(
    private val shuffleFunctions: ShuffleFunctions
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    // ВЕСЬ пул пар (все страницы)
    private var allPairs: List<Pair<Word, Word>> = emptyList()

    // Пагинация
    private val pageSize = 6
    private var currentPage = 0
    private var currentPagePairs: List<Pair<Word, Word>> = emptyList()

    // Трекинг собранных пар на текущей странице — по id
    private val matchedIdsOnPage = mutableSetOf<Int>()

    // Состояние для UI
    private val _state = MutableLiveData(
        IngameWordsState(
            selectedWords = emptyList(),
            errorWords = emptyList(),
            correctWords = emptyList(),
            usedWords = emptyList(),
            // добавим поле locked в data-класс если его ещё нет
            locked = false
        )
    )
    val state: LiveData<IngameWordsState> = _state

    // Текущая страница для адаптера
    private val _pagePairs = MutableLiveData<List<Pair<Word, Word>>>()
    val pagePairs: LiveData<List<Pair<Word, Word>>> = _pagePairs

    private val _events = MutableLiveData<AnswerEvent>()
    val events: LiveData<AnswerEvent> = _events

    // Закончен весь пул?
    private val _completed = MutableLiveData(false)
    val completed: LiveData<Boolean> = _completed

    fun setPool(all: List<Pair<Word, Word>>) {
        allPairs = all
        openPage(0)
    }

    private fun openPage(page: Int) {
        if (allPairs.isEmpty()) { _completed.value = true; return }

        val from = page * pageSize
        if (from >= allPairs.size) { _completed.value = true; return }

        val to = (from + pageSize).coerceAtMost(allPairs.size)
        currentPagePairs = allPairs.subList(from, to)
        currentPage = page
        matchedIdsOnPage.clear()

        _state.value = IngameWordsState(
            selectedWords = emptyList(),
            errorWords = emptyList(),
            correctWords = emptyList(),
            usedWords = emptyList(),
            locked = false
        )
        val shuffledPairs = shuffleFunctions.shufflePairs(currentPagePairs)
        _pagePairs.value = shuffledPairs
    }

    fun onWordClick(clickedWord: Word) {
        val s = _state.value ?: return
        if (s.locked) return

        val selected = s.selectedWords
        when (selected.size) {
            0 -> _state.value = s.copy(selectedWords = listOf(clickedWord))
            1 -> {
                // замена выбора на той же стороне
                if (clickedWord.language == selected[0].language && clickedWord.id != selected[0].id) {
                    _state.value = s.copy(selectedWords = listOf(clickedWord))
                }
                // снятие выбора той же карточкой
                else if (clickedWord.id == selected[0].id && clickedWord.language == selected[0].language) {
                    _state.value = s.copy(selectedWords = emptyList())
                }
                // проверяем пару
                else {
                    checkPair(selected[0], clickedWord)
                }
            }
            else -> _state.value = s.copy(selectedWords = emptyList())
        }
    }

    private fun checkPair(a: Word, b: Word) {
        val s = _state.value ?: return
        if (a.id == b.id) {
            Log.d("DEBUG", "PAIR IS CORRECT: $a $b")
            // правильная пара — подсветим, залочим клики, начислим и готовим перенос
            _events.value = AnswerEvent.CORRECT
            val id = a.id
            matchedIdsOnPage += id

            _state.value = s.copy(
                selectedWords = emptyList(),
                errorWords = emptyList(),
                correctWords = s.correctWords + listOf(a, b),
                locked = true
            )

            handler.postDelayed({
                val cur = _state.value ?: return@postDelayed
                val newUsed = cur.usedWords.toMutableList().apply {
                    add(a); add(b)
                }
                Log.d("DEBUG", "usedWords: $a $b")
                _state.value = cur.copy(
                    correctWords = emptyList(),
                    usedWords = newUsed,
                    locked = false
                )

                // если собраны все пары на странице — следующая страница
                if (matchedIdsOnPage.size >= currentPagePairs.size) {
                    openPage(currentPage + 1)
                }
            }, TimeReactionConstants.REACTION)
        } else {
            // ошибка — подсветка и сброс
            _events.value = AnswerEvent.WRONG
            _state.value = s.copy(
                selectedWords = emptyList(),
                errorWords = listOf(a, b),
                locked = true
            )
            handler.postDelayed({
                val cur = _state.value ?: return@postDelayed
                _state.value = cur.copy(errorWords = emptyList(), locked = false)
            }, TimeReactionConstants.REACTION)
        }
        Log.d("DEBUG", "usedWordsLIST: ${_state.value?.usedWords}")
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(null)
        super.onCleared()
    }

}
