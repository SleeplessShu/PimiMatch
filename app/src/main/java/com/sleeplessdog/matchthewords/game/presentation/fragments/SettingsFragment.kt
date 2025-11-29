package com.sleeplessdog.matchthewords.game.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.sleeplessdog.matchthewords.R
import com.sleeplessdog.matchthewords.databinding.SettingsFragmentBinding
import com.sleeplessdog.matchthewords.databinding.ViewDifficultyCardEasyBinding
import com.sleeplessdog.matchthewords.databinding.ViewDifficultyCardExpertBinding
import com.sleeplessdog.matchthewords.databinding.ViewDifficultyCardHardBinding
import com.sleeplessdog.matchthewords.databinding.ViewDifficultyCardMediumBinding
import com.sleeplessdog.matchthewords.game.domain.models.LanguageLevel
import com.sleeplessdog.matchthewords.game.presentation.holders.CategoriesBottomSheet
import com.sleeplessdog.matchthewords.game.presentation.models.CategoryUi
import com.sleeplessdog.matchthewords.game.presentation.models.DifficultLevel
import com.sleeplessdog.matchthewords.gameSelect.controller.LanguageAdapter
import com.sleeplessdog.matchthewords.gameSelect.controller.toFlagSmallRes
import com.sleeplessdog.matchthewords.utils.SupportFunctions
import org.koin.androidx.viewmodel.ext.android.viewModel

enum class LanguageAdapterState {
    UI, STUDY
}

class SettingsFragment : Fragment(R.layout.settings_fragment) {

    private val vm: SettingsViewModel by viewModel()
    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!

    private var currentLangMode: LanguageAdapterState = LanguageAdapterState.STUDY

    private lateinit var chipA1: Chip
    private lateinit var chipA2: Chip
    private lateinit var chipB1: Chip
    private lateinit var chipB2: Chip
    private lateinit var chipC1: Chip
    private lateinit var chipC2: Chip

    private lateinit var cardEasy: ViewDifficultyCardEasyBinding
    private lateinit var cardMedium: ViewDifficultyCardMediumBinding
    private lateinit var cardHard: ViewDifficultyCardHardBinding
    private lateinit var cardExpert: ViewDifficultyCardExpertBinding

    private lateinit var langAdapter: LanguageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = SettingsFragmentBinding.bind(view)
        setupLevelChips()
        setupLanguageList()
        setupDifficultyCards()
        setupObservers()
    }

    private fun setupLevelChips() {
        chipA1 = binding.btnA1
        chipA2 = binding.btnA2
        chipB1 = binding.btnB1
        chipB2 = binding.btnB2
        chipC1 = binding.btnC1
        chipC2 = binding.btnC2
    }

    private fun setupDifficultyCards() {
        cardEasy = binding.cardEasy
        cardMedium = binding.cardMedium
        cardHard = binding.cardHard
        cardExpert = binding.cardExpert
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.state.collect { state ->
                renderFeatured(state.featured)
            }
        }

        vm.levels.observe(viewLifecycleOwner) { selected ->
            chipA1.isChecked = LanguageLevel.A1 in selected
            chipA2.isChecked = LanguageLevel.A2 in selected
            chipB1.isChecked = LanguageLevel.B1 in selected
            chipB2.isChecked = LanguageLevel.B2 in selected
            chipC1.isChecked = LanguageLevel.C1 in selected
            chipC2.isChecked = LanguageLevel.C2 in selected
        }

        chipA1.setOnClickListener { vm.toggleLevel(LanguageLevel.A1) }
        chipA2.setOnClickListener { vm.toggleLevel(LanguageLevel.A2) }
        chipB1.setOnClickListener { vm.toggleLevel(LanguageLevel.B1) }
        chipB2.setOnClickListener { vm.toggleLevel(LanguageLevel.B2) }
        chipC1.setOnClickListener { vm.toggleLevel(LanguageLevel.C1) }
        chipC2.setOnClickListener { vm.toggleLevel(LanguageLevel.C2) }

        cardEasy.root.setOnClickListener {
            vm.onDifficultyPicked(DifficultLevel.EASY)
        }
        cardMedium.root.setOnClickListener {
            vm.onDifficultyPicked(DifficultLevel.MEDIUM)
        }
        cardHard.root.setOnClickListener {
            vm.onDifficultyPicked(DifficultLevel.HARD)
        }
        cardExpert.root.setOnClickListener {
            vm.onDifficultyPicked(DifficultLevel.EXPERT)
        }

        binding.ivFlagStudy.setOnClickListener {
            showLanguages(LanguageAdapterState.STUDY)
        }

        binding.ivFlagUi.setOnClickListener {
            Log.d("DEBUG", "flagclick: ")
            showLanguages(LanguageAdapterState.UI)
        }

        binding.languagesBackgroundSolid.setOnClickListener {
            hideAllLanguageLists()
        }
        binding.languagesBackground.setOnClickListener {
            hideAllLanguageLists()
        }

        vm.difficulty.observe(viewLifecycleOwner) { level ->

            cardEasy.root.isChecked = (level == DifficultLevel.EASY)
            cardMedium.root.isChecked = (level == DifficultLevel.MEDIUM)
            cardHard.root.isChecked = (level == DifficultLevel.HARD)
            cardExpert.root.isChecked = (level == DifficultLevel.EXPERT)
        }

        binding.btnShowAllCategories.setOnClickListener {
            val preselected =
                vm.state.value.user.plus(vm.state.value.defaults).filter { it.isSelected }
                    .map { it.key }.toSet()

            CategoriesBottomSheet.newInstance(preselected)
                .show(parentFragmentManager, "categoriesSheet")
        }

        parentFragmentManager.setFragmentResultListener(
            CategoriesBottomSheet.RESULT_KEY, viewLifecycleOwner
        ) { _, bundle ->
            val selected =
                bundle.getStringArrayList(CategoriesBottomSheet.ARG_SELECTED)?.toSet() ?: emptySet()
            vm.onSave(selected)
        }

        vm.studyLanguage.observe(viewLifecycleOwner) { study ->
            binding.ivFlagStudy.setImageResource(study.toFlagSmallRes())
        }

        vm.uiLanguage.observe(viewLifecycleOwner) { uiLang ->
            binding.ivFlagUi.setImageResource(uiLang.toFlagSmallRes())
        }

        vm.uiLanguageList.observe(viewLifecycleOwner) { list ->
            if (currentLangMode == LanguageAdapterState.UI && binding.rvLanguageList.visibility == View.VISIBLE) {
                val selected = vm.uiLanguage.value
                langAdapter.submit(list, selected)
                selected?.let { langAdapter.setSelected(it) }
            }
        }

        vm.studyLanguageList.observe(viewLifecycleOwner) { list ->
            if (currentLangMode == LanguageAdapterState.STUDY && binding.rvLanguageList.visibility == View.VISIBLE) {
                val selected = vm.studyLanguage.value
                langAdapter.submit(list, selected)
                selected?.let { langAdapter.setSelected(it) }
            }
        }
    }

    private fun setupLanguageList() {
        langAdapter = LanguageAdapter { picked ->
            vm.onLanguagePicked(picked, currentLangMode)
            langAdapter.setSelected(picked)
            binding.rvLanguageList.postDelayed({
                hideAllLanguageLists()
            }, 150)
        }

        binding.rvLanguageList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = langAdapter
        }
    }

    private fun renderFeatured(list: List<CategoryUi>) {
        val group = binding.cgFeaturedCategories
        group.removeAllViews()

        list.forEach { item ->
            val chip = SupportFunctions.createCategoryChip(group, item)

            chip.isChecked = item.isSelected
            chip.setOnClickListener { vm.onToggle(item.key) }

            group.addView(chip)
        }
    }

    private fun showBgIfNeeded() {
        val bg = binding.languagesBackground
        if (bg.visibility != View.VISIBLE) {
            bg.visibility = View.VISIBLE
            bg.alpha = 0f
            bg.animate().alpha(1f).setDuration(200).start()
        }

        val bgSolid = binding.languagesBackgroundSolid
        if (bgSolid.visibility != View.VISIBLE) {
            bgSolid.visibility = View.VISIBLE
            bgSolid.alpha = 0f
            bgSolid.animate().alpha(1f).setDuration(200).start()
        }
    }

    private fun hideBg() {
        val bg = binding.languagesBackground
        if (bg.visibility == View.VISIBLE) {
            bg.animate().alpha(0f).setDuration(200).withEndAction { bg.visibility = View.GONE }
                .start()
        }
        val bgSolid = binding.languagesBackgroundSolid
        if (bgSolid.visibility == View.VISIBLE) {
            bgSolid.animate().alpha(0f).setDuration(200).withEndAction { bgSolid.visibility = View.GONE }
                .start()
        }
    }

    private fun showLanguages(mode: LanguageAdapterState) {
        currentLangMode = mode
        val root = binding.languageSelectRoot
        if (root.visibility == View.VISIBLE && root.alpha == 1f) {
            hideAllLanguageLists()
        } else {
            root.visibility = View.VISIBLE
            root.alpha = 0f
            root.scaleY = 0f
            root.pivotY = 0f
            root.animate().alpha(1f).scaleY(1f).setDuration(200).start()
        }

        when (mode) {
            LanguageAdapterState.UI -> {
                binding.tvLanguageList.setText(R.string.int_language)
                val list = vm.uiLanguageList.value ?: emptyList()
                val selected = vm.uiLanguage.value
                langAdapter.submit(list, selected)
                selected?.let { langAdapter.setSelected(it) }
            }

            LanguageAdapterState.STUDY -> {
                binding.tvLanguageList.setText(R.string.std_language)
                val list = vm.studyLanguageList.value ?: emptyList()
                val selected = vm.studyLanguage.value
                langAdapter.submit(list, selected)
                selected?.let { langAdapter.setSelected(it) }
            }
        }
        showBgIfNeeded()
    }

    private fun hideAllLanguageLists() {
        val root = binding.languageSelectRoot

        if (root.visibility == View.VISIBLE) {
            root.animate().alpha(0f).scaleY(0f).setDuration(200)
                .withEndAction { root.visibility = View.GONE }.start()
        }
        hideBg()
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}