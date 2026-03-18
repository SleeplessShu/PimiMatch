package com.sleeplessdog.matchthewords.dictionary

enum class GroupsTab(
    val route: String,
    val label: String,
) {
    USERS("groups_users", "Мои"),
    GLOBAL("groups_global", "Стандартные")
}