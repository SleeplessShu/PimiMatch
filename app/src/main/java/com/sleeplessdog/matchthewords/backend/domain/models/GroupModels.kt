package com.sleeplessdog.matchthewords.backend.domain.models

data class CombinedGroupsDictionaryScreen(
    val userGroups: List<UserGroupUiEntity>,
    val globalGroups: List<GlobalGroupUiEntity>,
)

data class CombinedGroupsSettingsScreen(
    val featured: List<GroupUiSettings> = emptyList(),
    val userGroups: List<GroupUiSettings> = emptyList(),
    val globalGroups: List<GroupUiSettings> = emptyList(),
)


/**
 * используется в сеттингах, чтобы получить список всех групп
 */
data class GlobalGroupUiEntity(
    val groupId: String,        //технический id
    val title: String,          //то, что показываем пользователю
    val wordsCount: Int,
    val iconRes: Int?,
)

data class UserGroupUiEntity(
    val groupKey: String,
    val title: String,
    val wordsCount: Int? = null,
    val icon: Int,
)

data class GroupPresentationSettingsEntity(
    val key: String,
    val isSelected: Boolean,
    val isUser: Boolean,
    val orderInBlock: Int,
)

data class GroupUiSettings(
    val key: String,
    val titleRes: String,
    val iconRes: Int,
    val isSelected: Boolean,
    val isUser: Boolean,
    val orderInBlock: Int,
)

data class GlobalGroupDBEntity(
    val groupKey: String,
    val wordsCount: Int,
)

data class UserGroupDomainEntity(
    val groupKey: String,
    val title: String,
    val icon: String? = null,
)

data class GroupsUiState(
    val featured: List<GroupUiSettings> = emptyList(),
    val user: List<GroupUiSettings> = emptyList(),
    val defaults: List<GroupUiSettings> = emptyList(),
    val loading: Boolean = true,
    val error: Throwable? = null,
)