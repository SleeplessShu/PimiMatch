package com.sleeplessdog.matchthewords.game.domain.api

import com.sleeplessdog.matchthewords.game.data.WordEntity
import com.sleeplessdog.matchthewords.game.domain.models.LanguageLevel
import com.sleeplessdog.matchthewords.game.domain.models.WordsCategoriesList
import com.sleeplessdog.matchthewords.game.presentation.models.Language


interface DatabaseInteractor {
    suspend fun getWordsPack(
        language1: Language,
        language2: Language,
        level: LanguageLevel,
        difficultLevel: Int,
        category: WordsCategoriesList
    ): List<WordEntity>

    suspend fun updateWord(wordEntity: WordEntity)
}