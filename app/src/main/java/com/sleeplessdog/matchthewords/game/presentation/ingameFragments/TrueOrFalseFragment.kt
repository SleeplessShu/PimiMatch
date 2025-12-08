package com.sleeplessdog.matchthewords.game.presentation.ingameFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.sleeplessdog.matchthewords.R
import com.sleeplessdog.matchthewords.databinding.GameTrueOrFalseBinding
import com.sleeplessdog.matchthewords.game.presentation.GameViewModel
import com.sleeplessdog.matchthewords.game.presentation.controller.TOFButtonController
import com.sleeplessdog.matchthewords.game.presentation.controller.TOFCardBinder
import com.sleeplessdog.matchthewords.game.presentation.controller.TOFResultHighlighter
import com.sleeplessdog.matchthewords.game.presentation.controller.TOFSwipeController
import com.sleeplessdog.matchthewords.utils.ConstantsApp
import com.sleeplessdog.matchthewords.utils.ConstantsTimeReaction
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrueOrFalseFragment : Fragment(R.layout.game_true_or_false) {

    private val parentVM: GameViewModel by activityViewModel()
    private val childVM: TrueOrFalseViewModel by viewModel()

    private var _binding: GameTrueOrFalseBinding? = null
    private val binding get() = _binding!!

    private lateinit var topCard: View
    private lateinit var nextCard: View

    private var isLocked = false
    private var currentIsCorrect = false

    //Контроллеры
    private lateinit var buttonController: TOFButtonController
    private lateinit var swipeController: TOFSwipeController
    private lateinit var resultHighlighter: TOFResultHighlighter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = GameTrueOrFalseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        topCard = binding.wordCardA.root
        nextCard = binding.wordCardB.root
        nextCard.visibility = View.INVISIBLE

        setupControllers()
        setupObservers()
    }


    //ИНИЦИАЛИЗАЦИЯ КОНТРОЛЛЕРОВ
    private fun setupControllers() {
        buttonController = TOFButtonController(requireContext()) { isRight ->
            commitAnswer(isRight)
        }

        buttonController.bind(
            trueRoot = binding.btnTrue,
            trueIcon = binding.icTrue,
            falseRoot = binding.btnFalse,
            falseIcon = binding.icFalse
        )

        swipeController = TOFSwipeController(
            canSwipe = { childVM.ui.value?.locked == false },
            onTrue = { commitAnswer(true) },
            onFalse = { commitAnswer(false) })

        swipeController.attach(topCard)

        resultHighlighter = TOFResultHighlighter(requireContext())
    }

    //OBSERVERS
    private fun setupObservers() {
        parentVM.wordsPairs.observe(viewLifecycleOwner) { pool ->
            if (pool.isNullOrEmpty()) return@observe
            childVM.setPool(pool)
            binding.root.isVisible = true
        }

        childVM.ui.observe(viewLifecycleOwner) { ui ->
            if (ui == null) return@observe

            currentIsCorrect = ui.isCorrect

            TOFCardBinder.bind(topCard, ui, requireContext())

            buttonController.reset()
            isLocked = ui.locked
        }

        childVM.events.observe(viewLifecycleOwner) { e ->
            parentVM.onGameEvent(e)
        }
    }

    //ОСНОВНАЯ ЛОГИКА ОТВЕТА
    private fun commitAnswer(isRight: Boolean) {
        if (isLocked) return
        isLocked = true

        val preview = childVM.peekNext()
        if (preview != null) {
            TOFCardBinder.bind(nextCard, preview, requireContext())
            nextCard.apply {
                visibility = View.VISIBLE
                alpha = ConstantsApp.EMPTY_ALPHA
                translationY = ConstantsApp.CARD_PREVIEW_TRANSLATION_Y
                scaleX = ConstantsApp.CARD_PREVIEW_SCALE
                scaleY = ConstantsApp.CARD_PREVIEW_SCALE
            }
        }

        val ok = (isRight == currentIsCorrect)

        resultHighlighter.highlightCard(topCard, ok)

        viewLifecycleOwner.lifecycleScope.launch {
            delay(ConstantsTimeReaction.PAUSE_BEFORE_REACTION)

            animateParallelSwap(isRight) {
                if (isRight) childVM.onTrueClicked() else childVM.onFalseClicked()
                childVM.advanceNow()

                buttonController.reset()
                swapCards()
                swipeController.attach(topCard)

                isLocked = false
            }
        }
    }

    // АНИМАЦИЯ
    private fun animateParallelSwap(isRight: Boolean, end: () -> Unit) {
        val dir = if (isRight) 1 else -1

        topCard.animate().translationX(dir * topCard.width.toFloat())
            .translationY(-topCard.height * ConstantsApp.SWIPE_VERTICAL_FACTOR)
            .rotation(dir * ConstantsApp.SWIPE_ROTATION_DEGREES).alpha(ConstantsApp.EMPTY_ALPHA)
            .setDuration(ConstantsApp.CARD_SWAP_DURATION_MS).start()

        nextCard.animate().alpha(ConstantsApp.FULL_ALPHA).translationY(ConstantsApp.ZERO_SCALE)
            .scaleX(ConstantsApp.FULL_SCALE).scaleY(ConstantsApp.FULL_SCALE)
            .setDuration(ConstantsApp.CARD_SWAP_DURATION_MS).withEndAction(end).start()
    }

    private fun swapCards() {
        topCard.apply {
            translationX = 0f
            translationY = 0f
            rotation = 0f
            alpha = 1f
            visibility = View.INVISIBLE
            background = requireContext().getDrawable(R.drawable.bg_tof_card_r24)
            swipeController.attach(this)
        }

        val tmp = topCard
        topCard = nextCard
        nextCard = tmp
    }
}
