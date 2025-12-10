package com.sleeplessdog.matchthewords.game.presentation.controller

import com.sleeplessdog.matchthewords.R
import com.sleeplessdog.matchthewords.databinding.ItemDifficultyCardBinding
import com.sleeplessdog.matchthewords.game.presentation.models.DifficultLevel

class DifficultyCardController(
    private val easy: ItemDifficultyCardBinding,
    private val medium: ItemDifficultyCardBinding,
    private val hard: ItemDifficultyCardBinding,
    private val expert: ItemDifficultyCardBinding,
    private val onPick: (DifficultLevel) -> Unit
) {

    init {
        setupCard(
            binding = easy,
            iconRes = R.drawable.ic_game_lightning_1,
            titleRes = R.string.difficulty_easy,
            subtitleRes = R.string.difficulty_easy_words,
            level = DifficultLevel.EASY
        )

        setupCard(
            binding = medium,
            iconRes = R.drawable.ic_game_lightning_2,
            titleRes = R.string.difficulty_medium,
            subtitleRes = R.string.difficulty_medium_words,
            level = DifficultLevel.MEDIUM
        )

        setupCard(
            binding = hard,
            iconRes = R.drawable.ic_game_lightning_3,
            titleRes = R.string.difficulty_hard,
            subtitleRes = R.string.difficulty_hard_words,
            level = DifficultLevel.HARD
        )

        setupCard(
            binding = expert,
            iconRes = R.drawable.ic_game_lightning_4,
            titleRes = R.string.difficulty_expert,
            subtitleRes = R.string.difficulty_expert_words,
            level = DifficultLevel.EXPERT
        )
    }

    private fun setupCard(
        binding: ItemDifficultyCardBinding,
        iconRes: Int,
        titleRes: Int,
        subtitleRes: Int,
        level: DifficultLevel
    ) {
        binding.ivIcon.setImageResource(iconRes)
        binding.tvTitle.setText(titleRes)
        binding.tvSubtitle.setText(subtitleRes)
        binding.root.setOnClickListener { onPick(level) }
    }

    fun bind(current: DifficultLevel) {
        easy.root.isChecked = current == DifficultLevel.EASY
        medium.root.isChecked = current == DifficultLevel.MEDIUM
        hard.root.isChecked = current == DifficultLevel.HARD
        expert.root.isChecked = current == DifficultLevel.EXPERT
    }
}
