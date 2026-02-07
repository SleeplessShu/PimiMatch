package com.sleeplessdog.matchthewords.utils

import android.content.Context
import com.sleeplessdog.matchthewords.R


fun Context.groupTitleRes(key: String): String {
    if (key == "saved_words") {
        return getString(R.string.group_saved)
    }

    val normalized = normalizeGroupKey(key)

    val resId = resources.getIdentifier(
        "group_$normalized",
        "string",
        packageName
    )

    return if (resId != 0) getString(resId) else key
}

fun Context.groupIconRes(key: String): Int {
    val normalized = normalizeGroupKey(key)

    return resources.getIdentifier("ic_group_$normalized", "drawable", packageName)
        .takeIf { it != 0 } ?: R.drawable.ic_group_default
}

private fun normalizeGroupKey(key: String): String {
    return GROUP_KEY_ALIAS[key] ?: key
}

private val GROUP_KEY_ALIAS = mapOf(
    "general_adjectives" to "adjectives",
    "general_adverbs" to "adverbs",
    "general_pronouns" to "pronouns"
)