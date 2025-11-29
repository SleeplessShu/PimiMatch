package com.sleeplessdog.matchthewords.game.presentation.models

data class CategoriesUiState(
    val featured: List<CategoryUi> = emptyList(),
    val user: List<CategoryUi> = emptyList(),
    val defaults: List<CategoryUi> = emptyList(),
    val loading: Boolean = true,
    val error: Throwable? = null
)