package com.sleeplessdog.matchthewords.dictionary.group_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleeplessdog.matchthewords.backend.data.repository.AppPrefs
import com.sleeplessdog.matchthewords.backend.domain.usecases.GetGlobalGroupWordsOnceUC
import com.sleeplessdog.matchthewords.backend.domain.usecases.ObserveWordsInUserGroupUC
import com.sleeplessdog.matchthewords.dictionary.dictionary_screen.DictionaryDestinations.ARG_GROUP_ID
import com.sleeplessdog.matchthewords.dictionary.dictionary_screen.DictionaryDestinations.ARG_GROUP_NAME
import com.sleeplessdog.matchthewords.dictionary.dictionary_screen.DictionaryDestinations.ARG_GROUP_TYPE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GroupViewModel(
    private val observeWordsInUserGroup: ObserveWordsInUserGroupUC,
    private val getGlobalGroupWordsOnce: GetGlobalGroupWordsOnceUC,
    private val appPrefs: AppPrefs,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val ui = appPrefs.getUiLanguage()
    val study = appPrefs.getStudyLanguage()
    private val groupId: String = savedStateHandle.get<String>(ARG_GROUP_ID) ?: ""

    private val groupTitle: String = savedStateHandle.get<String>(ARG_GROUP_NAME) ?: ""

    private val groupType: GroupType =
        savedStateHandle.get<String>(ARG_GROUP_TYPE)
            ?.let { GroupType.valueOf(it) }
            ?: GroupType.GLOBAL

    private val _state = MutableStateFlow(GroupScreenState())
    val state: StateFlow<GroupScreenState> = _state

    init {
        when (groupType) {
            GroupType.USER -> observeUserGroup()
            GroupType.GLOBAL -> loadGlobalGroupOnce()
        }
    }

    private fun observeUserGroup() {
        viewModelScope.launch {
            observeWordsInUserGroup(groupId, ui = ui, study = study).collect { words ->

                _state.value = GroupScreenState(
                    groupId = groupId,
                    groupTitle = groupTitle,
                    words = words,
                    wordsCount = words.size,
                    loading = false
                )
            }
        }
    }

    private fun loadGlobalGroupOnce() {
        viewModelScope.launch {
            val words = getGlobalGroupWordsOnce(groupId, ui = ui, study = study)

            _state.value = GroupScreenState(
                groupId = groupId,
                groupTitle = groupTitle,
                words = words,
                wordsCount = words.size,
                loading = false
            )
        }
    }

    fun onAddWordClick() {
        TODO()
    }

    fun onEditWordClick() {
        TODO()
    }

    fun onDeleteWordClick() {
        TODO()
    }
}

