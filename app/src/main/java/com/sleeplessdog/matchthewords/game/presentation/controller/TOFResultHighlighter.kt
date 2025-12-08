package com.sleeplessdog.matchthewords.game.presentation.controller

import android.content.Context
import androidx.core.graphics.drawable.DrawableCompat
import com.sleeplessdog.matchthewords.R
import com.sleeplessdog.matchthewords.utils.ConstantsApp
import com.sleeplessdog.matchthewords.utils.SupportFunctions

class TOFResultHighlighter(
    private val context: Context
) {
    fun highlightCard(card: android.view.View, isCorrect: Boolean) {
        val color = if (isCorrect) R.color.green_primary
        else R.color.red_primary

        (card.background?.mutate())?.let { d ->
            DrawableCompat.setTint(
                d, SupportFunctions.colorWithAlpha(
                    context.getColor(color), ConstantsApp.RESULT_HIGHLIGHT_ALPHA
                )
            )
            card.background = d
        }
    }
}
