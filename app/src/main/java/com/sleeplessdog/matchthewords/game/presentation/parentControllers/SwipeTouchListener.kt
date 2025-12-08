package com.sleeplessdog.matchthewords.game.presentation.parentControllers

import android.view.MotionEvent
import android.view.View
import com.sleeplessdog.matchthewords.utils.ConstantsApp
import com.sleeplessdog.matchthewords.utils.ConstantsApp.SWIPE_COMMIT_THRESHOLD
import com.sleeplessdog.matchthewords.utils.ConstantsApp.SWIPE_DRAG_LIMIT
import com.sleeplessdog.matchthewords.utils.ConstantsApp.SWIPE_MAX_ROTATION
import com.sleeplessdog.matchthewords.utils.ConstantsApp.SWIPE_VERTICAL_TRANSLATION_FACTOR
import kotlin.math.abs

class SwipeTouchListener(
    private val card: View,
    private val onSwipeRightCommit: () -> Unit,
    private val onSwipeLeftCommit: () -> Unit,
    private val canSwipe: () -> Boolean = { true }
) : View.OnTouchListener {

    private var downX = ConstantsApp.ZERO_SCALE
    private var downY = ConstantsApp.ZERO_SCALE
    private var isSwiping = false

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (!canSwipe()) return false

        return when (event.actionMasked) {

            MotionEvent.ACTION_DOWN -> {
                downX = event.rawX
                downY = event.rawY
                isSwiping = false
                true
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = event.rawX - downX
                val dy = event.rawY - downY

                if (!isSwiping && abs(dy) > abs(dx)) {
                    false
                } else {
                    isSwiping = true

                    val clamped = dx.coerceIn(
                        -SWIPE_DRAG_LIMIT,
                        SWIPE_DRAG_LIMIT
                    )

                    card.translationX = dx
                    card.translationY =
                        -abs(clamped) * SWIPE_VERTICAL_TRANSLATION_FACTOR

                    card.rotation =
                        (clamped / SWIPE_DRAG_LIMIT) * SWIPE_MAX_ROTATION

                    true
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (!isSwiping) {
                    reset()
                    false
                } else {
                    val dx = event.rawX - downX

                    val commitRight = dx > SWIPE_COMMIT_THRESHOLD
                    val commitLeft = dx < -SWIPE_COMMIT_THRESHOLD

                    when {
                        commitRight -> onSwipeRightCommit()
                        commitLeft -> onSwipeLeftCommit()
                        else -> reset()
                    }
                    true
                }
            }

            else -> false
        }
    }


    private fun reset() {
        card.animate()
            .translationX(ConstantsApp.ZERO_SCALE)
            .translationY(ConstantsApp.ZERO_SCALE)
            .rotation(ConstantsApp.ZERO_SCALE)
            .setDuration(ConstantsApp.SWIPE_RESET_DURATION_MS)
            .start()
    }
}
