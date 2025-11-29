package com.sleeplessdog.matchthewords.di

import com.sleeplessdog.matchthewords.game.data.repositories.WordCategoriesRepositoryImpl
import com.sleeplessdog.matchthewords.game.domain.api.ScoreInteractor
import com.sleeplessdog.matchthewords.game.domain.interactors.ScoreInteractorImpl
import com.sleeplessdog.matchthewords.game.domain.interactors.WordsController
import com.sleeplessdog.matchthewords.game.domain.usecase.CreateUserCategoryUC
import com.sleeplessdog.matchthewords.game.domain.usecase.DeleteUserCategoryUC
import com.sleeplessdog.matchthewords.game.domain.usecase.ObserveAllCategoriesGroupedUC
import com.sleeplessdog.matchthewords.game.domain.usecase.ObserveFeaturedCategoriesUC
import com.sleeplessdog.matchthewords.game.domain.usecase.SaveSelectionUC
import com.sleeplessdog.matchthewords.game.domain.usecase.ToggleCategoryUC
import com.sleeplessdog.matchthewords.game.domain.repositories.WordCategoriesRepository
import com.sleeplessdog.matchthewords.server.domain.ServerDateInteractor
import com.sleeplessdog.matchthewords.server.domain.ServerDateInteractorImpl
import com.sleeplessdog.matchthewords.settings.domain.api.SettingsInteractor
import com.sleeplessdog.matchthewords.settings.domain.api.SharingInteractor
import com.sleeplessdog.matchthewords.settings.domain.interactors.SettingsInteractorImpl
import com.sleeplessdog.matchthewords.settings.domain.interactors.SharingInteractorImpl
import org.koin.dsl.module

val domainModule = module {

    single<SharingInteractor> {
        SharingInteractorImpl(get(), get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }


    single <ScoreInteractor> {
        ScoreInteractorImpl(get())
    }

    single <ServerDateInteractor> {
        ServerDateInteractorImpl(get())
    }

    single <WordsController> {
        WordsController(get())
    }
}