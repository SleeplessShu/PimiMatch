package com.sleeplessdog.matchthewords.game.presentation.controller

import android.view.View
import com.sleeplessdog.matchthewords.game.presentation.parentControllers.SwipeTouchListener

class TOFSwipeController(
    private val canSwipe: () -> Boolean,
    private val onTrue: () -> Unit,
    private val onFalse: () -> Unit
) {

    fun attach(card: View) {
        card.setOnTouchListener(
            SwipeTouchListener(
                card = card,
                onSwipeRightCommit = { onTrue() },
                onSwipeLeftCommit = { onFalse() },
                canSwipe = canSwipe
            )
        )
    }
}
