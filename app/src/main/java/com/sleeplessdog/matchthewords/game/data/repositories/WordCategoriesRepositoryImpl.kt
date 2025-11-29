package com.sleeplessdog.matchthewords.game.data.repositories

import com.sleeplessdog.matchthewords.game.data.WordCategoryEntity
import com.sleeplessdog.matchthewords.game.data.database.WordCategoryDao
import com.sleeplessdog.matchthewords.game.domain.models.WordsCategoriesList
import com.sleeplessdog.matchthewords.game.domain.models.toDomain
import com.sleeplessdog.matchthewords.game.domain.repositories.CategoriesGrouped
import com.sleeplessdog.matchthewords.game.domain.repositories.WordCategoriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class WordCategoriesRepositoryImpl(
    private val dao: WordCategoryDao
) : WordCategoriesRepository {

    override fun observeFeatured(limit: Int) =
        dao.observeFeatured(limit).map { it.map(WordCategoryEntity::toDomain) }

    override fun observeAll() =
        dao.observeAll().map { it.map(WordCategoryEntity::toDomain) }

    override fun observeAllGrouped(): Flow<CategoriesGrouped> =
        dao.observeAll().map { list ->
            val domain = list.map(WordCategoryEntity::toDomain)
            CategoriesGrouped(
                user = domain.filter { it.isUser },
                defaults = domain.filter { !it.isUser }
            )
        }

    override suspend fun toggleSelection(key: String) {
        val all = dao.observeAll().first()            // поток → снимем первый снепшот
        val randomKey = WordsCategoriesList.RANDOM.key

        // текущий выбранный ключи
        val selected = all.filter { it.isSelected }.map { it.key }.toMutableSet()

        if (selected.contains(key)) {
            // снимаем выбор
            selected.remove(key)
        } else {
            // ставим выбор
            selected.add(key)
        }

        val normalized: Set<String> =
            if (selected.isEmpty()) {
                // если вообще ничего не выбрано → включаем RANDOM
                setOf(randomKey)
            } else {
                // есть хотя бы одна категория → RANDOM исключаем
                selected - randomKey
            }

        dao.replaceSelection(normalized)
    }

    override suspend fun saveSelection(selectedKeys: Set<String>) {
        val randomKey = WordsCategoriesList.RANDOM.key
        val normalized: Set<String> =
            if (selectedKeys.isEmpty()) {
                setOf(randomKey)
            } else {
                selectedKeys - randomKey
            }

        dao.replaceSelection(normalized)
    }

    override suspend fun upsertUserCategory(
        key: String,
        titleKey: String,
        iconKey: String,
        orderInBlock: Int
    ) {
        dao.upsert(
            WordCategoryEntity(
                key = key,
                titleKey = titleKey,
                iconKey = iconKey,
                isSelected = true,   // обычно создаём и сразу отмечаем
                isUser = true,
                orderInBlock = orderInBlock
            )
        )
    }

    override suspend fun deleteUserCategory(key: String) {
        dao.deleteUserCategory(key)
    }
}
