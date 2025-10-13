package com.sleeplessdog.matchthewords.game.presentation.fragments

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sleeplessdog.matchthewords.game.presentation.models.AnswerEvent
import com.sleeplessdog.matchthewords.game.presentation.models.Word
import com.sleeplessdog.matchthewords.game.presentation.models.WriteTheWordLetterUi
import com.sleeplessdog.matchthewords.game.presentation.models.WriteTheWordUi
import com.sleeplessdog.matchthewords.utils.TimeReactionConstants

class WriteTheWordViewModel : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private var pool: List<Pair<Word, Word>> = emptyList()
    private var used = mutableSetOf<Int>()

    private val _ui = MutableLiveData(WriteTheWordUi())
    val ui: LiveData<WriteTheWordUi> = _ui

    private val _events = MutableLiveData<AnswerEvent>()
    val events: LiveData<AnswerEvent> = _events

    private val _completed = MutableLiveData(false)
    val completed: LiveData<Boolean> = _completed

    // текущие данные вопроса
    private var targetRaw = ""          // исходный перевод (как пришёл)
    private var targetClean = ""        // часть ДО '/' (обрезанная)
    private var letters: MutableList<WriteTheWordLetterUi> = mutableListOf()

    fun setPool(pairs: List<Pair<Word, Word>>) {
        pool = pairs
        used.clear()
        _completed.value = false
        nextQuestion()
    }

    private fun nextQuestion() {
        if (pool.isEmpty() || used.size >= pool.size) {
            _completed.value = true; return
        }
        val available = pool.indices.filterNot { it in used }
        if (available.isEmpty()) { _completed.value = true; return }

        val index = available.random()
        used += index
        val (word, translationWord) = pool[index]
        createPair(prompt = word.text, translation = translationWord.text)
    }

    private fun createPair(prompt: String, translation: String) {
        targetRaw = translation
        targetClean = cleanTranslation(translation)

        // генерим буквы именно из "чистой" строки ДО слеша
        letters = targetClean.mapIndexed { i, c -> WriteTheWordLetterUi(i, c) }
            .shuffled()
            .toMutableList()

        _ui.value = WriteTheWordUi(
            prompt = prompt,
            target = targetClean,            // показываем и в UI, чтобы было видно "эталон" (если нужно)
            input = "",
            letters = letters,
            locked = false
        )
    }

    /** оставляем часть ДО '/', плюс trim */
    private fun cleanTranslation(raw: String): String {
        val beforeSlash = raw.substringBefore('/')
        return beforeSlash.trim()
    }

    fun onLetterClick(position: Int) {
        val state = _ui.value ?: return
        if (state.locked) return

        val l = letters[position]
        if (l.used) return               // уже использована — игнор

        letters[position] = l.copy(used = true)
        _ui.value = state.copy(
            input = state.input + l.char,
            letters = letters.toList()
        )
    }

    fun onBackspace() {
        val state = _ui.value ?: return
        if (state.locked || state.input.isEmpty()) return

        // снимаем used с последней добавленной буквы по символу
        val lastChar = state.input.last()
        val idx = letters.indexOfLast { it.used && it.char == lastChar }
        if (idx != -1) {
            letters[idx] = letters[idx].copy(used = false)
            _ui.value = state.copy(
                input = state.input.dropLast(1),
                letters = letters.toList()
            )
        }
    }

    fun onClear() {
        val state = _ui.value ?: return
        letters = letters.map { it.copy(used = false) }.toMutableList()
        _ui.value = state.copy(input = "", letters = letters.toList())
    }

    fun onCheck() {
        val state = _ui.value ?: return

        // сравниваем с targetClean (часть ДО '/'), регистр не важен
        val ok = state.input.equals(targetClean, ignoreCase = true)

        _ui.value = state.copy(locked = true)
        _events.value = if (ok) AnswerEvent.CORRECT else AnswerEvent.WRONG

        handler.postDelayed({ nextQuestion() }, TimeReactionConstants.NEXT_QUESTION)
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(null)
        super.onCleared()
    }
}
