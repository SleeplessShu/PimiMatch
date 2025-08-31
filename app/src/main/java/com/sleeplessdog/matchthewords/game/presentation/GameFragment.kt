package com.sleeplessdog.matchthewords.game.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sleeplessdog.matchthewords.R
import com.sleeplessdog.matchthewords.databinding.GameFragmentBinding
import com.sleeplessdog.matchthewords.game.presentation.fragments.EndGameFragment
import com.sleeplessdog.matchthewords.game.presentation.fragments.LoadingFragment
import com.sleeplessdog.matchthewords.game.presentation.fragments.MatchSettingsFragment
import com.sleeplessdog.matchthewords.game.presentation.fragments.WordsMatchingFragment
import com.sleeplessdog.matchthewords.game.presentation.models.GameState
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameFragment : Fragment() {
    private val viewModel: GameViewModel by viewModel()
    private var _binding: GameFragmentBinding? = null
    private val binding: GameFragmentBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = GameFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }


    private fun setupObservers() {
        viewModel.gameState.observe(viewLifecycleOwner) { newState ->
            Log.d("DEBUG", "setupObservers: ${newState.state} ")
            when (newState.state) {

                GameState.MATCH_SETTINGS -> {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.flFragmentContainer, MatchSettingsFragment()).commit()
                    binding.tvHeader.setText(R.string.state_title_match_settings)
                }

                GameState.LOADING -> {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.flFragmentContainer, LoadingFragment()).commit()
                    binding.tvHeader.setText(R.string.state_title_loading)
                }

                GameState.GAME -> {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.flFragmentContainer, WordsMatchingFragment()).commit()
                    binding.tvHeader.setText(R.string.empty)
                }

                GameState.END_OF_GAME -> {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.flFragmentContainer, EndGameFragment()).commit()
                    binding.tvHeader.setText(R.string.empty)

                }
            }
        }
    }
}