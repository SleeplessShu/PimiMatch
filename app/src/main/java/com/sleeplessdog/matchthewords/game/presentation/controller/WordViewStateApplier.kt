package com.sleeplessdog.matchthewords.game.presentation.controller

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.sleeplessdog.matchthewords.game.presentation.models.ButtonState

object WordViewStateApplier {

    fun apply(
        button: TextView,
        pimi: View,
        state: ButtonState,
        isUsed: Boolean,
        ctx: Context
    ) {
        button.setBackgroundResource(state.backgroundRes)
        button.isEnabled = state.enabled
        button.setTextColor(ContextCompat.getColor(ctx, state.textColorRes))

        if (isUsed) {
            button.setTextColor(Color.TRANSPARENT)
            pimi.visibility = View.VISIBLE
        } else {
            pimi.visibility = View.GONE
        }
    }
}
