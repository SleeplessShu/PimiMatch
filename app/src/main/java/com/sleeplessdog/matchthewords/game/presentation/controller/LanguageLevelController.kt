package com.sleeplessdog.matchthewords.game.presentation.controller

import com.sleeplessdog.matchthewords.game.domain.models.LanguageLevel
import com.sleeplessdog.matchthewords.game.domain.models.LanguageLevelChips

class LanguageLevelController(
    private val chips: LanguageLevelChips,
    private val onToggle: (LanguageLevel) -> Unit
) {

    init {
        chips.a1.setOnClickListener { onToggle(LanguageLevel.A1) }
        chips.a2.setOnClickListener { onToggle(LanguageLevel.A2) }
        chips.b1.setOnClickListener { onToggle(LanguageLevel.B1) }
        chips.b2.setOnClickListener { onToggle(LanguageLevel.B2) }
        chips.c1.setOnClickListener { onToggle(LanguageLevel.C1) }
        chips.c2.setOnClickListener { onToggle(LanguageLevel.C2) }
    }

    fun bindLevels(selected: Set<LanguageLevel>) {
        chips.a1.isChecked = LanguageLevel.A1 in selected
        chips.a2.isChecked = LanguageLevel.A2 in selected
        chips.b1.isChecked = LanguageLevel.B1 in selected
        chips.b2.isChecked = LanguageLevel.B2 in selected
        chips.c1.isChecked = LanguageLevel.C1 in selected
        chips.c2.isChecked = LanguageLevel.C2 in selected
    }
}
