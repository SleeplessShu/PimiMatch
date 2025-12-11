package com.sleeplessdog.matchthewords.game.domain.interactors

import com.sleeplessdog.matchthewords.game.domain.usecase.CreateUserCategoryUC
import com.sleeplessdog.matchthewords.game.domain.usecase.ObserveAllCategoriesGroupedUC
import com.sleeplessdog.matchthewords.game.domain.usecase.ObserveFeaturedCategoriesUC
import com.sleeplessdog.matchthewords.game.domain.usecase.SaveSelectionUC
import com.sleeplessdog.matchthewords.game.domain.usecase.ToggleCategoryUC

class SettingsInteractor(
    private val observeFeaturedUC: ObserveFeaturedCategoriesUC,
    private val observeAllGroupedUC: ObserveAllCategoriesGroupedUC,
    private val toggleUC: ToggleCategoryUC,
    private val saveSelectionUC: SaveSelectionUC,
    private val createUserUC: CreateUserCategoryUC,
) {
    fun featured(limit: Int) = observeFeaturedUC(limit)

    fun allGrouped() = observeAllGroupedUC()

    suspend fun toggle(key: String) = toggleUC(key)

    suspend fun saveSelection(keys: Set<String>) = saveSelectionUC(keys)

    suspend fun createCategory(key: String, titleKey: String, iconKey: String) =
        createUserUC(key, titleKey, iconKey)
}
