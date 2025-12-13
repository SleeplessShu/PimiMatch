package com.sleeplessdog.matchthewords.di

import com.sleeplessdog.matchthewords.game.domain.api.ScoreInteractor
import com.sleeplessdog.matchthewords.game.domain.interactors.ScoreInteractorImpl
import com.sleeplessdog.matchthewords.game.domain.interactors.WordsController
import com.sleeplessdog.matchthewords.server.domain.ServerDateInteractor
import com.sleeplessdog.matchthewords.server.domain.ServerDateInteractorImpl
import org.koin.dsl.module

val domainModule =
    module {
        single<ScoreInteractor> {
            ScoreInteractorImpl(
                scoreRepository = get(),
            )
        }

        single<ServerDateInteractor> {
            ServerDateInteractorImpl(
                dateRepository = get(),
            )
        }

        single<WordsController> {
            WordsController(
                repository = get(),
            )
        }
    }
