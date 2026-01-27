package com.sleeplessdog.matchthewords.dictionary.adding_new_group

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleeplessdog.matchthewords.backend.data.db.user.UserWordEntity
import com.sleeplessdog.matchthewords.backend.data.repository.AppPrefs
import com.sleeplessdog.matchthewords.backend.data.repository.WordsRepository
import com.sleeplessdog.matchthewords.backend.domain.usecases.groups.CreateUserGroupUC
import com.sleeplessdog.matchthewords.backend.domain.usecases.groups.GetWordsCountForGroupUC
import com.sleeplessdog.matchthewords.backend.domain.usecases.groups.ObserveAllGroupsGroupedUC
import com.sleeplessdog.matchthewords.dictionary.DictionaryScreenState
import com.sleeplessdog.matchthewords.dictionary.GroupUiDictionary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserGroupViewModel(
    private val repository: WordsRepository
) : ViewModel() {
    private val _wordsFlow = MutableStateFlow<List<UserWordEntity>>(emptyList())
    val wordsFlow: StateFlow<List<UserWordEntity>> = _wordsFlow

    fun loadWordsForGroup(groupKey: String) {
        viewModelScope.launch {
            val words = repository getWordsForGroup(groupKey)
            _wordsFlow.value = words
        }
    }
}