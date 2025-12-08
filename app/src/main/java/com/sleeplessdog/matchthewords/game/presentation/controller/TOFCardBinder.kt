package com.sleeplessdog.matchthewords.game.presentation.controller

import android.content.Context
import android.view.View
import android.widget.TextView
import com.sleeplessdog.matchthewords.R
import com.sleeplessdog.matchthewords.game.presentation.models.TfQuestionUi

object TOFCardBinder {

    fun bind(card: View, model: TfQuestionUi, context: Context) {
        card.findViewById<TextView>(R.id.t_word).text = model.word.text
        card.findViewById<TextView>(R.id.t_translate).text = model.shownTranslation.text

        card.background = context.getDrawable(R.drawable.bg_tof_card_r24)
    }
}
