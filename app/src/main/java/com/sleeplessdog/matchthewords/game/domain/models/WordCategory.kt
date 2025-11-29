package com.sleeplessdog.matchthewords.game.domain.models

import com.sleeplessdog.matchthewords.game.data.WordCategoryEntity

data class WordCategory(
    val key: String,
    val titleKey: String,
    val iconKey: String,
    val isSelected: Boolean,
    val isUser: Boolean,
    val orderInBlock: Int
)

fun WordCategoryEntity.toDomain() = WordCategory(
    key, titleKey, iconKey, isSelected, isUser, orderInBlock
)
