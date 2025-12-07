package com.sleeplessdog.matchthewords.game.presentation.parentControllers

import android.widget.ImageView
import androidx.core.view.isVisible
import com.sleeplessdog.matchthewords.R
import com.sleeplessdog.matchthewords.game.presentation.parentModels.HeartState
import com.sleeplessdog.matchthewords.utils.ConstantsApp

class HeartsController(
    private val hearts: List<ImageView>
) {

    private val heartStates: Map<Int, List<HeartState>> = mapOf(
        ConstantsApp.MAX_HEARTS to listOf(
            HeartState.FULL,
            HeartState.FULL,
            HeartState.FULL
        ),
        2 to listOf(
            HeartState.HALF,
            HeartState.HALF,
            HeartState.HIDDEN
        ),
        1 to listOf(
            HeartState.LOW,
            HeartState.HIDDEN,
            HeartState.HIDDEN
        ),
        ConstantsApp.MIN_HEARTS to listOf(
            HeartState.HIDDEN,
            HeartState.HIDDEN,
            HeartState.HIDDEN
        )
    )

    fun render(heartsQuantity: Int) {
        val states = heartStates[
            heartsQuantity.coerceIn(
                ConstantsApp.MIN_HEARTS,
                ConstantsApp.MAX_HEARTS
            )
        ] ?: return

        hearts.zip(states).forEach { (iv, st) ->
            applyHeartAnimated(iv, st)
        }
    }

    private fun applyHeartAnimated(iv: ImageView, state: HeartState) {
        when (state) {
            HeartState.FULL -> showHeart(iv, R.drawable.int_lives_3)
            HeartState.HALF -> showHeart(iv, R.drawable.int_lives_2)
            HeartState.LOW  -> showHeart(iv, R.drawable.int_lives_1)
            HeartState.HIDDEN -> hideHeart(iv)
        }
    }

    private fun showHeart(iv: ImageView, res: Int) {
        iv.setImageResource(res)

        if (!iv.isVisible) {
            iv.alpha = ConstantsApp.EMPTY_ALPHA
            iv.isVisible = true
            iv.animate()
                .alpha(ConstantsApp.FULL_ALPHA)
                .setDuration(ConstantsApp.HEART_SHOW_DURATION_MS)
                .start()
        }
    }

    private fun hideHeart(iv: ImageView) {
        if (iv.isVisible) {
            iv.animate()
                .alpha(ConstantsApp.EMPTY_ALPHA)
                .setDuration(ConstantsApp.HEART_HIDE_DURATION_MS)
                .withEndAction { iv.isVisible = false }
                .start()
        }
    }
}
