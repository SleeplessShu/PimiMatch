package com.sleeplessdog.matchthewords.settings.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.sleeplessdog.matchthewords.databinding.SettingsFragmentBinding
import com.sleeplessdog.matchthewords.utils.DebounceClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class SettingsFragment : Fragment() {
    private val viewModelDB: DatabaseViewModel by viewModel()
    private val viewModel: SettingsViewModel by viewModel()
    private var _binding: SettingsFragmentBinding? = null
    private val binding: SettingsFragmentBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()


        binding.switcherTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setTheme(isChecked)
        }


        binding.bMailToSupport.setOnClickListener(
            DebounceClickListener(viewLifecycleOwner) {
                viewModel.supportSend()
            })

        binding.bShareApp.setOnClickListener(
            DebounceClickListener(viewLifecycleOwner) {
                viewModel.shareApp()
            })

        binding.bDatabase.setOnClickListener(
            DebounceClickListener(viewLifecycleOwner) {
                viewModelDB.checkForDatabaseUpdate()
            })
    }


    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            updateUi(state)
        }

        viewModelDB.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is DbUpdateState.Loading -> showLoading()
                is DbUpdateState.UpToDate -> hideLoading()
                is DbUpdateState.UpdateAvailable -> showUpdateDialog(state.serverDate)
                is DbUpdateState.Success -> {
                    showSuccessMessage()
                    updateDatabaseDate()
                }

                is DbUpdateState.Error -> showError(state.message)
            }

        }
    }


    private fun updateUi(state: ThemeViewState) {
        binding.switcherTheme.isChecked = (state.isNightModeOn)
        updateDatabaseDate()
    }

    private fun updateDatabaseDate() {
        binding.tvDatabaseVersion.setText(viewModelDB.getCurrentDatabaseDate())
    }

    private fun showUpdateDialog(serverDate: String) {
        AlertDialog.Builder(requireContext()).setTitle("Update Available")
            .setMessage("New database is available. Download?").setPositiveButton("Yes") { _, _ ->
                val file = File(requireContext().filesDir, "latest_db.db")
                viewModelDB.downloadDatabase(file, serverDate)
            }.setNegativeButton("No", null).show()
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showSuccessMessage() {
        Toast.makeText(requireContext(), "Database updated successfully!", Toast.LENGTH_SHORT)
            .show()

    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_LONG).show()
    }
}
