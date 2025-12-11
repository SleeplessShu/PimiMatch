package com.sleeplessdog.matchthewords.game.presentation.models

import android.view.View
import com.google.android.flexbox.FlexboxLayout


data class TopicsMenuViews(
    val root: View,
    val background: View,
    val header: View,
    val categoriesScroll: View,
    val bottomButtons: View,
    val groupUser: FlexboxLayout,
    val groupDefault: FlexboxLayout,
    val btnShowAll: View,
    val btnCancel: View,
    val btnSave: View
)
