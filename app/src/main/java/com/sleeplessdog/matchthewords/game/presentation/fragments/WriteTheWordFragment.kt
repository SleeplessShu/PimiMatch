package com.sleeplessdog.matchthewords.game.presentation.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.sleeplessdog.matchthewords.R
import com.sleeplessdog.matchthewords.databinding.WriteTheWordFragmentBinding
import com.sleeplessdog.matchthewords.game.presentation.GameViewModel
import com.sleeplessdog.matchthewords.game.presentation.holders.LettersAdapter
import com.sleeplessdog.matchthewords.game.presentation.models.AnswerEvent
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WriteTheWordFragment : Fragment(R.layout.write_the_word_fragment) {
    private val parentVM: GameViewModel by sharedViewModel(
        owner = { requireParentFragment() })
    private val childVM: WriteTheWordViewModel by viewModel()
    private var _binding: WriteTheWordFragmentBinding? = null
    private val binding: WriteTheWordFragmentBinding get() = _binding!!
    private lateinit var adapter: LettersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = WriteTheWordFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        adapter = LettersAdapter(
            onClick = { pos -> childVM.onLetterClick(pos) }
        )
        binding.rvLetters.adapter = adapter

        val flex = com.google.android.flexbox.FlexboxLayoutManager(requireContext()).apply {
            flexDirection = com.google.android.flexbox.FlexDirection.ROW
            flexWrap = com.google.android.flexbox.FlexWrap.WRAP
            justifyContent = com.google.android.flexbox.JustifyContent.CENTER
        }
        binding.rvLetters.layoutManager = flex

        setupObservers()
    }

    override fun onDestroyView() {
        _binding = null;
        super.onDestroyView()
    }

    private fun setupObservers() {
        parentVM.wordsPairs.observe(viewLifecycleOwner) { pairs ->
            if (!pairs.isNullOrEmpty()) {
                childVM.setPool(pairs)
                binding.root.isVisible = true
            }
        }

        childVM.ui.observe(viewLifecycleOwner) { ui ->
            binding.tvPrompt.text = ui.prompt
            binding.tvInput.text = ui.input
            adapter.locked = ui.locked
            adapter.submitList(ui.letters)
            binding.btnBackspace.isEnabled = !ui.locked && ui.input.isNotEmpty()
            binding.btnClear.isEnabled = !ui.locked && ui.input.isNotEmpty()
            binding.btnCheck.isEnabled = !ui.locked && ui.input.isNotEmpty()
        }


        childVM.events.observe(viewLifecycleOwner) { ev ->
            when (ev) {
                AnswerEvent.CORRECT -> parentVM.reactOnCorrect()
                AnswerEvent.WRONG   -> parentVM.reactOnError()
            }
        }


        childVM.completed.observe(viewLifecycleOwner) { done ->
            if (done == true) parentVM.onGameEnd()
        }

        binding.btnBackspace.setOnClickListener { childVM.onBackspace() }
        binding.btnClear.setOnClickListener { childVM.onClear() }
        binding.btnCheck.setOnClickListener { childVM.onCheck() }
    }
}

