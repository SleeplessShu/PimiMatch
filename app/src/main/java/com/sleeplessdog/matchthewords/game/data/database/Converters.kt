package com.sleeplessdog.matchthewords.game.data.database

import androidx.room.TypeConverter
import com.sleeplessdog.matchthewords.game.domain.models.LanguageLevel
import com.sleeplessdog.matchthewords.game.domain.models.WordsCategoriesList

class Converters {
    @TypeConverter fun fromCategory(v: WordsCategoriesList): String = v.name
    @TypeConverter fun toCategory(v: String): WordsCategoriesList = WordsCategoriesList.valueOf(v.uppercase())

    @TypeConverter fun fromLevel(v: LanguageLevel): String = v.name
    @TypeConverter fun toLevel(v: String): LanguageLevel = LanguageLevel.valueOf(v.uppercase())
}