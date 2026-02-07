package com.sleeplessdog.matchthewords.game.presentation.fragments

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleeplessdog.matchthewords.backend.data.repository.AppPrefs
import com.sleeplessdog.matchthewords.backend.domain.models.CombinedGroupsSettingsScreen
import com.sleeplessdog.matchthewords.backend.domain.models.GroupUiSettings
import com.sleeplessdog.matchthewords.backend.domain.models.LanguageLevel
import com.sleeplessdog.matchthewords.backend.domain.usecases.CreateUserGroupUC
import com.sleeplessdog.matchthewords.backend.domain.usecases.ObserveAllGroupsGroupedUC
import com.sleeplessdog.matchthewords.backend.domain.usecases.SaveSelectionUC
import com.sleeplessdog.matchthewords.backend.domain.usecases.ToggleCategoryUC
import com.sleeplessdog.matchthewords.backend.domain.usecases.settings.SettingsObserveLevelsUC
import com.sleeplessdog.matchthewords.backend.domain.usecases.settings.SettingsSaveLevelsUC
import com.sleeplessdog.matchthewords.game.presentation.holders.LanguageAdapterState
import com.sleeplessdog.matchthewords.game.presentation.models.DifficultLevel
import com.sleeplessdog.matchthewords.game.presentation.models.Language
import com.sleeplessdog.matchthewords.utils.SupportFunctions.drawableIdByName
import com.sleeplessdog.matchthewords.utils.SupportFunctions.stringByName
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    //private val observeFeaturedUC: ObserveFeaturedGroupsUC,
    observeAllGroups: ObserveAllGroupsGroupedUC,
    private val toggleUC: ToggleCategoryUC,
    private val saveSelectionUC: SaveSelectionUC,
    private val createUserGroupUC: CreateUserGroupUC,
    private val saveLevelsUC: SettingsSaveLevelsUC,
    private val observeLevelsUC: SettingsObserveLevelsUC,
    private val app: Application,
    private val appPrefs: AppPrefs,
) : ViewModel() {
    private val _uiLanguage = MutableLiveData<Language>()
    val uiLanguage: LiveData<Language> = _uiLanguage

    private val _studyLanguage = MutableLiveData<Language>()
    val studyLanguage: LiveData<Language> = _studyLanguage

    private val _uiLanguageList = MutableLiveData<List<Language>>()
    val uiLanguageList: LiveData<List<Language>> = _uiLanguageList

    private val _studyLanguageList = MutableLiveData<List<Language>>()
    val studyLanguageList: LiveData<List<Language>> = _studyLanguageList


    private val _levels = MutableLiveData<Set<LanguageLevel>>()
    val levels: LiveData<Set<LanguageLevel>> = _levels

    private val _difficulty = MutableLiveData<DifficultLevel>()
    val difficulty: LiveData<DifficultLevel> = _difficulty


    val state: StateFlow<CombinedGroupsSettingsScreen> =
        observeAllGroups()
            .map { domain ->
                CombinedGroupsSettingsScreen(
                    featured = domain.featured.map { it.toUi() },
                    userGroups = domain.userGroups.map { it.toUi() },
                    globalGroups = domain.globalGroups.map { it.toUi() }
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = CombinedGroupsSettingsScreen()
            )

    init {
        viewModelScope.launch {
            appPrefs.observeStudyLanguage().collect { newLanguage ->
                _studyLanguage.value = newLanguage
            }
        }

        viewModelScope.launch {
            appPrefs.observeUiLanguage().collect { newLanguage ->
                _uiLanguage.value = newLanguage
            }
        }

        val ui = appPrefs.getUiLanguage()
        val study = appPrefs.getStudyLanguage()
        _uiLanguage.value = ui
        _studyLanguage.value = study
        rebuild(ui, study)

        viewModelScope.launch {
            observeLevelsUC.observe()
                .collect { levels ->
                    _levels.value = levels.ifEmpty { setOf(LanguageLevel.A1) }
                }
        }

        loadDifficulty()
    }

    fun onToggle(key: String) = viewModelScope.launch {
        toggleUC(key)
    }

    fun onSave(selectedKeys: Set<String>) = viewModelScope.launch {
        saveSelectionUC(selectedKeys)
    }

    fun onLanguagePicked(newLang: Language, currentLangMode: LanguageAdapterState) {
        when (currentLangMode) {
            LanguageAdapterState.UI -> {
                val study = _studyLanguage.value ?: Language.SPANISH
                _uiLanguage.value = newLang
                appPrefs.save(newLang, study)
                rebuild(newLang, study)
            }

            LanguageAdapterState.STUDY -> {
                val ui = _uiLanguage.value ?: Language.RUSSIAN
                _studyLanguage.value = newLang
                appPrefs.save(ui, newLang)
                rebuild(ui, newLang)
            }
        }
    }

    fun loadDifficulty() {
        _difficulty.value = appPrefs.getDifficulty()
    }

    fun onDifficultyPicked(level: DifficultLevel) {
        _difficulty.value = level
        appPrefs.saveDifficulty(level)
    }

    fun toggleLevel(level: LanguageLevel) {
        val current = _levels.value ?: setOf(LanguageLevel.A1)

        val new = if (current.contains(level)) current - level
        else current + level

        if (new.isEmpty()) {
            Toast.makeText(app, "Надо выбрать хотя бы один", Toast.LENGTH_LONG).show()
            return
        }

        _levels.value = new

        viewModelScope.launch {
            saveLevelsUC.save(new)
        }

    }

    private fun rebuild(ui: Language, study: Language) {
        _uiLanguageList.value = Language.entries.filter { it != study }
        _studyLanguageList.value = Language.entries.filter { it != ui }
    }

    private fun GroupUiSettings.toUi(): GroupUiSettings {
        val uiLang = appPrefs.getUiLanguage()

        return GroupUiSettings(
            key = key,
            titleRes = app.stringByName(key, uiLang),
            iconRes = app.drawableIdByName(key),
            isSelected = isSelected,
            isUser = isUser,
            orderInBlock = orderInBlock
        )
    }

    private companion object {
        val FEATURED_LIMIT = 8
    }
}
