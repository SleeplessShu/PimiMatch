package com.sleeplessdog.matchthewords.game.presentation.models

data class CategoryUi(
    val key: String,
    val title: String,
    val iconRes: Int,
    val isSelected: Boolean,
    val isUser: Boolean
)