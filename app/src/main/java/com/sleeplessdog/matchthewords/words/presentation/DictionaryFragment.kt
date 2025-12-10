package com.sleeplessdog.matchthewords.words.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sleeplessdog.matchthewords.databinding.DictionaryFragmentBinding

class DictionaryFragment : Fragment() {
    private var _binding: DictionaryFragmentBinding? = null
    private val binding: DictionaryFragmentBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = DictionaryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}
