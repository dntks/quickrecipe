package com.dtks.quickrecipe.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dtks.quickrecipe.domain.RecipeDomainEntity
import com.dtks.quickrecipe.ui.details.DetailsScreen
import com.dtks.quickrecipe.ui.navigation.QuickRecipeDestinationsArgs.RECIPE_ID_ARG
import com.dtks.quickrecipe.ui.navigation.QuickRecipeDestinationsArgs.USER_MESSAGE_ARG
import com.dtks.quickrecipe.ui.navigation.QuickRecipeScreens.DETAILS_SCREEN
import com.dtks.quickrecipe.ui.navigation.QuickRecipeScreens.OVERVIEW_SCREEN
import com.dtks.quickrecipe.ui.overview.OverviewScreen
import com.dtks.quickrecipe.viewmodel.overview.OverviewViewModel

@Composable
fun QuickRecipeNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = QuickRecipeDestinations.OVERVIEW_ROUTE,
    navActions: QuickRecipeNavigationActions = remember(navController) {
        QuickRecipeNavigationActions(navController)
    },
    overviewViewModel: OverviewViewModel = hiltViewModel(),
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {


        Scaffold(
        ) { paddingValues ->
            paddingValues.toString()
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = modifier
            ) {
                composable(
                    route = QuickRecipeDestinations.OVERVIEW_ROUTE,
                    arguments = listOf(
                        navArgument(USER_MESSAGE_ARG) {
                            type = NavType.IntType; defaultValue = 0
                        }
                    )
                ) {
                    OverviewScreen(
                        onRecipeClick = { recipe ->
                            navActions.navigateToRecipeDetails(
                                recipe
                            )
                        },
                        viewModel = overviewViewModel
                    )
                }
                composable(
                    route = QuickRecipeDestinations.DETAILS_ROUTE,
                ) {
                    DetailsScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

class QuickRecipeNavigationActions(private val navController: NavHostController) {
    fun navigateToRecipeDetails(recipe: RecipeDomainEntity) {
        navController.navigate("$DETAILS_SCREEN/${recipe.id}")
    }
}

object QuickRecipeScreens {
    const val OVERVIEW_SCREEN = "recipes_overview"
    const val DETAILS_SCREEN = "recipe_details"
}

object QuickRecipeDestinationsArgs {
    const val USER_MESSAGE_ARG = "userMessage"
    const val RECIPE_ID_ARG = "recipeId"
}

object QuickRecipeDestinations {
    const val OVERVIEW_ROUTE = OVERVIEW_SCREEN
    const val DETAILS_ROUTE = "$DETAILS_SCREEN/{$RECIPE_ID_ARG}"
}