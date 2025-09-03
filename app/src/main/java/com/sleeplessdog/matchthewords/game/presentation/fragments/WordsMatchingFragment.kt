package com.sleeplessdog.matchthewords.game.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sleeplessdog.matchthewords.App
import com.sleeplessdog.matchthewords.R
import com.sleeplessdog.matchthewords.databinding.WordsMatchingFragmentBinding
import com.sleeplessdog.matchthewords.game.presentation.GameViewModel
import com.sleeplessdog.matchthewords.game.presentation.holders.WordsMatchingAdapter
import com.sleeplessdog.matchthewords.game.presentation.models.Word
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class WordsMatchingFragment : Fragment(R.layout.words_matching_fragment) {
    private val parentViewModel: GameViewModel by sharedViewModel(
        owner = { requireParentFragment() }
    )
    private var _binding: WordsMatchingFragmentBinding? = null
    private val binding: WordsMatchingFragmentBinding get() = _binding!!
    private lateinit var adapter: WordsMatchingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = WordsMatchingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupUI()
    }

    private fun setupUI() {
        adapter = WordsMatchingAdapter(
            context = App.appContext,
            onWordClick = {word: Word -> parentViewModel.onWordClick(word)}
            )
        binding.rvWordsList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWordsList.adapter = adapter
    }

    private fun setupObservers() {
        parentViewModel.wordsPairs.observe(viewLifecycleOwner) { wordsPairs ->
            adapter.updateWordsList(wordsPairs)
        }

        parentViewModel.ingameWordsState.observe(viewLifecycleOwner) { ingameWordState ->
            Log.d("DEBUG", "setupObservers:\n" +
                    "selected ${ingameWordState.selectedWords}\n" +
                    "correct ${ingameWordState.correctWords}\n" +
                    "used ${ingameWordState.usedWords}\n" +
                    "error ${ingameWordState.errorWords} ")
            adapter.updateSelectedWords(ingameWordState.selectedWords)
            adapter.updateErrorWords(ingameWordState.errorWords)
            adapter.updateCorrectWords(ingameWordState.correctWords)
            adapter.updateUsedWords(ingameWordState.usedWords)
        }


    }


}