package com.sleeplessdog.matchthewords.dictionary.group_screen

data class GroupScreenState(
    val groupId: String = "",
    val groupTitle: String = "",
    val words: List<WordUi> = emptyList(),
    val wordsCount: Int = 0,
    val loading: Boolean = true,
)