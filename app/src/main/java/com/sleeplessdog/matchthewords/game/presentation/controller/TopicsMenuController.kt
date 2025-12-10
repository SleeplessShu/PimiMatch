package com.sleeplessdog.matchthewords.game.presentation.controller

import android.view.View
import androidx.core.view.isVisible
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import com.sleeplessdog.matchthewords.game.presentation.models.CategoryUi
import com.sleeplessdog.matchthewords.utils.ConstantsApp
import com.sleeplessdog.matchthewords.utils.SupportFunctions

class TopicsMenuController(
    private val root: View,
    private val topicsBackground: View,
    private val header: View,
    private val categoriesScroll: View,
    private val bottomButtons: View,
    private val groupUser: FlexboxLayout,
    private val groupDefault: FlexboxLayout,
    btnShowAll: View,
    btnCancel: View,
    btnSave: View,
    private val getPreselectedKeys: () -> Set<String>,
    private val onSaveSelection: (Set<String>) -> Unit
) {

    private var preselected: Set<String> = emptySet()
    private var lastUser: List<CategoryUi> = emptyList()
    private var lastDefaults: List<CategoryUi> = emptyList()

    init {
        root.visibility = View.GONE

        btnShowAll.setOnClickListener {
            preselected = getPreselectedKeys()
            show()
            renderGroups()
        }

        btnCancel.setOnClickListener {
            hide()
        }

        topicsBackground.setOnClickListener {
            hide()
        }

        btnSave.setOnClickListener {
            val selected = readSelectedKeys()
            onSaveSelection(selected)
            hide()
        }
    }

    fun updateGroups(user: List<CategoryUi>, defaults: List<CategoryUi>) {
        lastUser = user
        lastDefaults = defaults
        if (root.isVisible) {
            renderGroups()
        }
    }

    // ---------- render ----------

    private fun renderGroups() {
        renderGroup(groupUser, lastUser)
        renderGroup(groupDefault, lastDefaults)
    }

    private fun renderGroup(group: FlexboxLayout, items: List<CategoryUi>) {
        group.removeAllViews()
        items.forEach { item ->
            group.addView(
                SupportFunctions.createCategoryChip(group, item).apply {
                    isChecked = item.key in preselected || item.isSelected
                }
            )
        }
    }

    private fun readSelectedKeys(): Set<String> {
        fun collect(group: FlexboxLayout): List<String> =
            (0 until group.childCount).mapNotNull { i ->
                val child = group.getChildAt(i)
                val chip = child as? Chip ?: return@mapNotNull null
                val key = chip.tag as? String ?: chip.text.toString()
                if (chip.isChecked) key else null
            }

        return (collect(groupUser) + collect(groupDefault)).toSet()
    }

    // ---------- animations ----------

    private fun show() {
        if (root.isVisible) return

        root.alpha = ConstantsApp.ZERO_SCALE
        root.visibility = View.VISIBLE
        root.animate()
            .alpha(ConstantsApp.FULL_ALPHA)
            .setDuration(ConstantsApp.ANIMATION_DURATION_FOREGROUND)
            .start()

        topicsBackground.apply {
            alpha = ConstantsApp.ZERO_SCALE
            visibility = View.VISIBLE
            animate()
                .alpha(ConstantsApp.FULL_ALPHA)
                .setDuration(ConstantsApp.ANIMATION_DURATION_BACKGROUND)
                .start()
        }

        val contentViews = listOf(header, categoriesScroll, bottomButtons)
        contentViews.forEach { view ->
            view.alpha = ConstantsApp.ZERO_SCALE
            view.translationY = ConstantsApp.TOPICS_MENU_CONTENT_OFFSET_Y
            view.animate()
                .alpha(ConstantsApp.FULL_ALPHA)
                .translationY(ConstantsApp.ZERO_SCALE)
                .setDuration(ConstantsApp.ANIMATION_DURATION_BACKGROUND)
                .start()
        }
    }

    private fun hide() {
        if (!root.isVisible) return

        topicsBackground.animate()
            .alpha(ConstantsApp.ZERO_SCALE)
            .setDuration(ConstantsApp.ANIMATION_DURATION_BACKGROUND)
            .withEndAction {
                topicsBackground.visibility = View.GONE
            }
            .start()

        val contentViews = listOf(header, categoriesScroll, bottomButtons)
        var finished = 0
        val total = contentViews.size

        contentViews.forEach { view ->
            view.animate()
                .alpha(ConstantsApp.ZERO_SCALE)
                .translationY(ConstantsApp.TOPICS_MENU_CONTENT_OFFSET_Y)
                .setDuration(ConstantsApp.ANIMATION_DURATION_BACKGROUND)
                .withEndAction {
                    finished++
                    if (finished == total) {
                        root.visibility = View.GONE

                        contentViews.forEach { v ->
                            v.alpha = ConstantsApp.FULL_ALPHA
                            v.translationY = ConstantsApp.ZERO_SCALE
                        }
                        topicsBackground.alpha = ConstantsApp.FULL_ALPHA
                    }
                }
                .start()
        }
    }
}
