package com.sleeplessdog.matchthewords.game.presentation.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sleeplessdog.matchthewords.R
import com.sleeplessdog.matchthewords.databinding.LoadingBinding
import com.sleeplessdog.matchthewords.utils.ConstantsApp

class LoadingFragment : Fragment(R.layout.loading) {

    private var _binding: LoadingBinding? = null
    private val binding: LoadingBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startLoadingAnimation()
    }

    private fun startLoadingAnimation() {
        val scaleFactor = ConstantsApp.LOADING_SCALE_FACTOR

        binding.starMain.pulse(
            scale = scaleFactor,
            durationMs = ConstantsApp.LOADING_PULSE_MAIN_DURATION,
            startDelayMs = ConstantsApp.LOADING_PULSE_MAIN_DELAY
        )

        binding.starTr.pulse(
            scale = scaleFactor,
            durationMs = ConstantsApp.LOADING_PULSE_TR_DURATION,
            startDelayMs = ConstantsApp.LOADING_PULSE_TR_DELAY
        )

        binding.starBl.pulse(
            scale = scaleFactor,
            durationMs = ConstantsApp.LOADING_PULSE_BL_DURATION,
            startDelayMs = ConstantsApp.LOADING_PULSE_BL_DELAY
        )

        binding.starBr.pulse(
            scale = scaleFactor,
            durationMs = ConstantsApp.LOADING_PULSE_BR_DURATION,
            startDelayMs = ConstantsApp.LOADING_PULSE_BR_DELAY
        )

        binding.starTl.pulse(
            scale = scaleFactor,
            durationMs = ConstantsApp.LOADING_PULSE_TL_DURATION,
            startDelayMs = ConstantsApp.LOADING_PULSE_TL_DELAY
        )
    }

    private fun View.pulse(
        scale: Float,
        durationMs: Long,
        startDelayMs: Long = ConstantsApp.ZERO_DURATION_MS
    ) {
        val scaleX = ObjectAnimator.ofFloat(
            this,
            View.SCALE_X,
            ConstantsApp.FULL_SCALE,
            scale,
            ConstantsApp.FULL_SCALE
        ).apply {
            duration = durationMs
            startDelay = startDelayMs
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
        }

        val scaleY = ObjectAnimator.ofFloat(
            this,
            View.SCALE_Y,
            ConstantsApp.FULL_SCALE,
            scale,
            ConstantsApp.FULL_SCALE
        ).apply {
            duration = durationMs
            startDelay = startDelayMs
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
        }

        AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            start()
        }
    }
}
