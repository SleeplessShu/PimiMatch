package com.sleeplessdog.matchthewords.dictionary.adding_new_group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyGroupComposeFragment : Fragment() {
    private val viewModel: MyGroupViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MyGroupUi(
                    viewModel = viewModel,
                    onBackClick = { findNavController().navigateUp() })
            }
        }
    }

}