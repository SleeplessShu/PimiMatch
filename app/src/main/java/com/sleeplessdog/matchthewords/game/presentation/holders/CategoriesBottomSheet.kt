package com.sleeplessdog.matchthewords.game.presentation.holders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.sleeplessdog.matchthewords.databinding.BsCategoriesContentBinding
import com.sleeplessdog.matchthewords.game.presentation.fragments.SettingsViewModel
import com.sleeplessdog.matchthewords.game.presentation.models.CategoryUi
import com.sleeplessdog.matchthewords.utils.SupportFunctions
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CategoriesBottomSheet(

) : BottomSheetDialogFragment() {

    private var _binding: BsCategoriesContentBinding? = null
    private val binding get() = _binding!!
    private val vm: SettingsViewModel by sharedViewModel() // общий с экраном

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BsCategoriesContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // предвыделенные из аргументов
        val preselected = arguments?.getStringArrayList(ARG_SELECTED)?.toSet() ?: emptySet()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.state.collect { s ->
                renderGroup(binding.cgUserCategories, s.user, preselected)
                renderGroup(binding.cgDefaultCategories, s.defaults, preselected)
            }
        }

        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnSave.setOnClickListener {
            val selectedKeys = readSelectedKeys()
            setFragmentResult(RESULT_KEY, bundleOf(ARG_SELECTED to ArrayList(selectedKeys)))
            dismiss()
        }
    }

    private fun renderGroup(group: FlexboxLayout, items: List<CategoryUi>, preselected: Set<String>) {
        group.removeAllViews()
        items.forEach { item ->
            group.addView(SupportFunctions.createCategoryChip(group, item).apply {
                isChecked = item.key in preselected || item.isSelected
            })
        }
    }

    private fun readSelectedKeys(): Set<String> {
        fun collect(group: FlexboxLayout): List<String> =
            (0 until group.childCount).mapNotNull { i ->
                val chip = group.getChildAt(i) as Chip
                val key = chip.tag as? String ?: chip.text.toString() // лучше хранить key в tag
                if (chip.isChecked) key else null
            }

        // складываем теги; при создании чипа поставь tag = item.key
        return (collect(binding.cgUserCategories) + collect(binding.cgDefaultCategories)).toSet()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val RESULT_KEY = "categories_result"
        const val ARG_SELECTED = "selected_keys"

        fun newInstance(preselected: Set<String>) = CategoriesBottomSheet().apply {
            arguments = bundleOf(ARG_SELECTED to ArrayList(preselected))
        }
    }

}
