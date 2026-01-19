package com.sleeplessdog.matchthewords.score.models

import com.sleeplessdog.matchthewords.score.domain.models.AwardId

data class AwardMeta(
    val id: AwardId = AwardId.I_UNDERSTOOD,
    val title: String = "",
    val description: String = "",
    val iconLocked: Int,
    val iconUnlocked: Int,
    val isLocked: Boolean = true,
)
