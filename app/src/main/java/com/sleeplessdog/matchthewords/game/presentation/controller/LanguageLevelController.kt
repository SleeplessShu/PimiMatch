package com.sleeplessdog.matchthewords.game.presentation.controller

import com.google.android.material.chip.Chip
import com.sleeplessdog.matchthewords.game.domain.models.LanguageLevel

class LanguageLevelController(
    private val chipA1: Chip,
    private val chipA2: Chip,
    private val chipB1: Chip,
    private val chipB2: Chip,
    private val chipC1: Chip,
    private val chipC2: Chip,
    private val onToggle: (LanguageLevel) -> Unit
) {

    init {
        chipA1.setOnClickListener { onToggle(LanguageLevel.A1) }
        chipA2.setOnClickListener { onToggle(LanguageLevel.A2) }
        chipB1.setOnClickListener { onToggle(LanguageLevel.B1) }
        chipB2.setOnClickListener { onToggle(LanguageLevel.B2) }
        chipC1.setOnClickListener { onToggle(LanguageLevel.C1) }
        chipC2.setOnClickListener { onToggle(LanguageLevel.C2) }
    }

    fun bindLevels(selected: Set<LanguageLevel>) {
        chipA1.isChecked = LanguageLevel.A1 in selected
        chipA2.isChecked = LanguageLevel.A2 in selected
        chipB1.isChecked = LanguageLevel.B1 in selected
        chipB2.isChecked = LanguageLevel.B2 in selected
        chipC1.isChecked = LanguageLevel.C1 in selected
        chipC2.isChecked = LanguageLevel.C2 in selected
    }
}
