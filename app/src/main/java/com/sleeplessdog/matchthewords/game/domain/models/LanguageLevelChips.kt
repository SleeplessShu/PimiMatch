package com.sleeplessdog.matchthewords.game.domain.models

import com.google.android.material.chip.Chip

data class LanguageLevelChips(
    val a1: Chip,
    val a2: Chip,
    val b1: Chip,
    val b2: Chip,
    val c1: Chip,
    val c2: Chip
) {

    fun getAll(): List<Chip> = listOf(a1, a2, b1, b2, c1, c2)

    fun get(level: LanguageLevel): Chip = when(level) {
        LanguageLevel.A1 -> a1
        LanguageLevel.A2 -> a2
        LanguageLevel.B1 -> b1
        LanguageLevel.B2 -> b2
        LanguageLevel.C1 -> c1
        LanguageLevel.C2 -> c2
    }
}
