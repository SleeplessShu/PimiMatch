package com.sleeplessdog.matchthewords.di


import com.sleeplessdog.matchthewords.backend.data.db.global.GlobalDatabase
import com.sleeplessdog.matchthewords.backend.data.db.user.UserDatabase
import com.sleeplessdog.matchthewords.backend.data.repository.GroupsRepository
import com.sleeplessdog.matchthewords.backend.data.repository.WordsRepository
import com.sleeplessdog.matchthewords.backend.domain.models.WordsController
import com.sleeplessdog.matchthewords.backend.domain.usecases.CreateUserGroupUC
import com.sleeplessdog.matchthewords.backend.domain.usecases.GetGlobalGroupsOnceUC
import com.sleeplessdog.matchthewords.backend.domain.usecases.GetSelectedGroupsUC
import com.sleeplessdog.matchthewords.backend.domain.usecases.GetWordsCountForGroupUC
import com.sleeplessdog.matchthewords.backend.domain.usecases.GetWordsCountUserGroupUC
import com.sleeplessdog.matchthewords.backend.domain.usecases.ObserveAllGroupsForDictionaryUC
import com.sleeplessdog.matchthewords.backend.domain.usecases.ObserveAllGroupsForSettingsUC
import com.sleeplessdog.matchthewords.backend.domain.usecases.ObserveUserGroupsUC
import com.sleeplessdog.matchthewords.backend.domain.usecases.SaveSelectionUC
import com.sleeplessdog.matchthewords.backend.domain.usecases.ToggleCategoryUC
import com.sleeplessdog.matchthewords.backend.domain.usecases.score.UpdateScoreProgressUseCase
import com.sleeplessdog.matchthewords.backend.domain.usecases.score.UpdateWordProgressUseCase
import com.sleeplessdog.matchthewords.backend.domain.usecases.settings.SettingsObserveLevelsUC
import com.sleeplessdog.matchthewords.backend.domain.usecases.settings.SettingsSaveLevelsUC
import com.sleeplessdog.matchthewords.backend.domain.usecases.words.AddWordToUserDictionaryUC
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    // -------- Global DB --------
    single {
        GlobalDatabase.create(androidContext())
    }

    single {
        get<GlobalDatabase>().globalDao()
    }

    // -------- User DB --------
    single {
        UserDatabase.create(androidContext())
    }

    single {
        get<UserDatabase>().userDao()
    }

    // -------- Repository --------
    single {
        WordsRepository(
            globalDao = get(),
            userDao = get()
        )
    }

    // -------- Controller --------
    single {
        WordsController(
            repository = get()
        )
    }

    single {
        UpdateWordProgressUseCase(
            userDao = get()
        )
    }

    single {
        AddWordToUserDictionaryUC(
            userDao = get()
        )
    }

    single { GroupsRepository(get(), get()) }



    single { ToggleCategoryUC(get()) }
    single { SaveSelectionUC(get()) }
    single { CreateUserGroupUC(get()) }
    single { SettingsSaveLevelsUC(get()) }
    single { SettingsObserveLevelsUC(get()) }
    single { UpdateScoreProgressUseCase(get()) }
    single { GetSelectedGroupsUC(get()) }
    single { GetWordsCountForGroupUC(get()) }
    single { GetGlobalGroupsOnceUC(get()) }
    single { GetWordsCountUserGroupUC(get()) }
    single { ObserveUserGroupsUC(get()) }
    single { ObserveAllGroupsForSettingsUC(get()) }
    single { ObserveAllGroupsForDictionaryUC(get()) }
}
