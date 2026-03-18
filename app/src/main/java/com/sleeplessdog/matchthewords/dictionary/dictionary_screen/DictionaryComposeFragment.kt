package com.sleeplessdog.matchthewords.dictionary.dictionary_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sleeplessdog.matchthewords.dictionary.DictionaryUi
import com.sleeplessdog.matchthewords.dictionary.group_screen.GroupType
import com.sleeplessdog.matchthewords.dictionary.group_screen.GroupUi
import com.sleeplessdog.matchthewords.dictionary.group_screen.GroupViewModel
import com.sleeplessdog.matchthewords.main.MainActivity
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DictionaryComposeFragment : Fragment() {

    private val viewModelDictionary: DictionaryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        return ComposeView(requireContext()).apply {

            val bottomPadding =
                (requireActivity() as MainActivity).getBottomNavHeight()

            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )

            setContent {

                val composeNavController = rememberNavController()

                NavHost(
                    navController = composeNavController,
                    startDestination = DictionaryDestinations.MAIN,

                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(400)
                        ) + fadeIn(tween(400))
                    },

                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(400)
                        ) + fadeOut(tween(400))
                    },

                    popEnterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(400)
                        ) + fadeIn(tween(400))
                    },

                    popExitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(400)
                        ) + fadeOut(tween(400))
                    }
                ) {

                    // =======================
                    // MAIN SCREEN
                    // =======================
                    composable(DictionaryDestinations.MAIN) {

                        DictionaryUi(
                            viewModel = viewModelDictionary,

                            onNavigateToUserGroup = { groupId, groupTitle ->
                                composeNavController.navigate(
                                    DictionaryDestinations.groupRoute(
                                        groupId, groupTitle, GroupType.USER
                                    )
                                )
                            },

                            onNavigateToGlobalGroup = { groupId, groupTitle ->
                                composeNavController.navigate(
                                    DictionaryDestinations.groupRoute(
                                        groupId, groupTitle,
                                        GroupType.GLOBAL
                                    )
                                )
                            },
                            bottomPadding = bottomPadding
                        )
                    }

                    // =======================
                    // GROUP SCREEN
                    // =======================
                    composable(
                        route = "${DictionaryDestinations.GROUP}/" +
                                "{${DictionaryDestinations.ARG_GROUP_ID}}/" +
                                "{${DictionaryDestinations.ARG_GROUP_NAME}}/" +
                                "{${DictionaryDestinations.ARG_GROUP_TYPE}}"
                    ) { backStackEntry ->

                        /* val groupId =
                             backStackEntry.arguments?.getString(
                                 DictionaryDestinations.ARG_GROUP_ID
                             ) ?: return@composable

                         val groupTitle =
                             backStackEntry.arguments?.getString(
                                 DictionaryDestinations.ARG_GROUP_NAME
                             ) ?: ""

                         val groupType = backStackEntry.arguments
                             ?.getString(DictionaryDestinations.ARG_GROUP_TYPE)
                             ?.let { GroupType.valueOf(it) }
                             ?: return@composable*/

                        val groupViewModel: GroupViewModel =
                            koinViewModel(viewModelStoreOwner = backStackEntry)

                        GroupUi(
                            onBackClick = { composeNavController.popBackStack() },
                            viewModel = groupViewModel
                        )
                    }
                }
            }
        }
    }
}
