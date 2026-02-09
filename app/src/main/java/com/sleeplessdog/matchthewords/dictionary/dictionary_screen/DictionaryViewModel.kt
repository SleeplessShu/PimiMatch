package com.sleeplessdog.matchthewords.dictionary.dictionary_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleeplessdog.matchthewords.backend.domain.models.CombinedGroupsDictionaryUi
import com.sleeplessdog.matchthewords.backend.domain.usecases.CreateUserGroupUC
import com.sleeplessdog.matchthewords.backend.domain.usecases.ObserveAllGroupsForDictionaryUC
import com.sleeplessdog.matchthewords.dictionary.GroupDictionaryUiMapper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DictionaryViewModel(
    observeAllGroups: ObserveAllGroupsForDictionaryUC,
    private val createUserGroup: CreateUserGroupUC,
    private val groupDictionaryUiMapper: GroupDictionaryUiMapper,

    /*private val getGlobalGroupsOnce: GetGlobalGroupsOnceUC,
    private val getWordsCountUserGroup: GetWordsCountUserGroupUC,
    private val observeUserGroups: ObserveUserGroupsUC,
    private val appPrefs: AppPrefs,
    private val app: Application,*/
) : ViewModel() {

    /*private val _categoriesGrouped = MutableStateFlow(
        CombinedGroupsDictionaryScreen(userGroups = emptyList(), globalGroups = emptyList())
    )
    val categoriesGrouped: StateFlow<CombinedGroupsDictionaryScreen> = _categoriesGrouped
*/
    val state: StateFlow<CombinedGroupsDictionaryUi> =
        observeAllGroups()
            .map { domain ->
                CombinedGroupsDictionaryUi(
                    userGroups = domain.userGroups.map(groupDictionaryUiMapper::map),
                    globalGroups = domain.globalGroups.map(groupDictionaryUiMapper::map)
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = CombinedGroupsDictionaryUi()
            )

    init {
        /*observeUserGroups()
        fetchGlobalGroups()*/
    }

    /*private fun observeUserGroups() {
        viewModelScope.launch {
            observeUserGroups.invoke().collect { userGroups ->
                val userUiGroups = userGroups.map { group ->
                    val icon =
                        if (group.icon == null) R.drawable.icon_book else app.groupIconRes(group.icon)
                    UserGroupUiEntity(
                        groupKey = group.groupKey,
                        title = group.title,
                        icon = icon,
                        wordsCount = getWordsCountUserGroup(group.groupKey)
                    )
                }

                _categoriesGrouped.update { currentState ->
                    currentState.copy(userGroups = userUiGroups)
                }
            }
        }
    }

    private fun fetchGlobalGroups() {
        viewModelScope.launch {

            val globalGroups = getGlobalGroupsOnce()
            val globalUiGroups = globalGroups.map { group ->
                val titleRes = app.groupTitleRes(group.groupKey)
                if (titleRes != 0) {
                    app.getString(titleRes)
                } else {
                    group.groupKey // fallback, НИКОГДА не падает
                }
                GlobalGroupUiEntity(
                    groupId = group.groupKey,
                    title = getGroupUiName(app, 0, group.groupKey),
                    iconRes = app.groupIconRes(group.groupKey),
                    wordsCount = group.wordsCount
                )
            }
            _categoriesGrouped.update { currentState ->
                currentState.copy(globalGroups = globalUiGroups)
            }
        }
    }*/

    fun addNewUserGroup(name: String) {
        viewModelScope.launch {
            createUserGroup(
                groupName = name,
            )
        }
    }
}