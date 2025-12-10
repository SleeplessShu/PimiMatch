package com.sleeplessdog.matchthewords.game.presentation.controller

import android.content.Context
import android.content.res.ColorStateList
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.sleeplessdog.matchthewords.R
import com.sleeplessdog.matchthewords.game.presentation.models.TOFButtonState

class TOFButtonController(
    private val context: Context,
    private val onClick: (Boolean) -> Unit
) {

    data class TofButton(
        val root: View,
        val icon: ImageView,
        val isTrue: Boolean
    )

    lateinit var btnTrue: TofButton
    lateinit var btnFalse: TofButton

    fun bind(
        trueRoot: View,
        trueIcon: ImageView,
        falseRoot: View,
        falseIcon: ImageView
    ) {
        btnTrue = TofButton(trueRoot, trueIcon, true)
        btnFalse = TofButton(falseRoot, falseIcon, false)

        trueRoot.setOnClickListener { press(btnTrue) }
        falseRoot.setOnClickListener { press(btnFalse) }

        attachPressEffect(trueRoot, btnTrue)
        attachPressEffect(falseRoot, btnFalse)
    }

    private fun press(button: TofButton) {
        button.applyState(TOFButtonState.PRESSED)
        onClick(button.isTrue)
    }

    private fun attachPressEffect(v: View, b: TofButton) {
        v.setOnTouchListener { view, e ->
            when (e.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    b.applyState(TOFButtonState.PRESSED)
                }

                MotionEvent.ACTION_UP -> {
                    b.applyState(TOFButtonState.DEFAULT)
                    view.performClick()
                }

                MotionEvent.ACTION_CANCEL -> {
                    b.applyState(TOFButtonState.DEFAULT)
                }
            }
            true
        }
    }


    fun reset() {
        btnTrue.applyState(TOFButtonState.DEFAULT)
        btnFalse.applyState(TOFButtonState.DEFAULT)
    }

    private fun TofButton.applyState(state: TOFButtonState) {
        root.setBackgroundResource(state.backgroundRes)

        val autoTint = if (isTrue)
            R.color.green_primary
        else
            R.color.red_primary

        val tint = when (state) {
            TOFButtonState.DEFAULT,
            TOFButtonState.PRESSED -> autoTint
            else -> state.tintColorRes
        }

        icon.imageTintList =
            ColorStateList.valueOf(context.getColor(tint))

        root.translationY = state.offsetY
    }
}
