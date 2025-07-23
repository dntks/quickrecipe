package com.dtks.quickrecipe.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.dtks.quickrecipe.ui.details.DetailsScreen
import com.dtks.quickrecipe.ui.overview.OverviewScreen
import com.dtks.quickrecipe.ui.viewmodel.details.DetailsViewModel
import com.dtks.quickrecipe.ui.viewmodel.overview.OverviewViewModel
import kotlinx.serialization.Serializable

@Serializable
data class Overview(
    val userMessage: String? = null
)

@Serializable
data class Details(
    val recipeId: Long
)

@Composable
fun QuickRecipeNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    overviewViewModel: OverviewViewModel = hiltViewModel(),
    detailsViewModel: DetailsViewModel = hiltViewModel(),
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold { paddingValues ->
            paddingValues.toString()
            NavHost(
                navController = navController,
                startDestination = Overview(),
                modifier = modifier
            ) {
                composable<Overview> {
                    OverviewScreen(
                        onRecipeClick = { recipe ->
                            navController.navigate(
                                route = Details(
                                    recipe.id
                                )
                            )
                        },
                        viewModel = overviewViewModel
                    )
                }
                composable<Details> { backStackEntry ->
                    val details: Details = backStackEntry.toRoute()
                    DetailsScreen(
                        onBack = { navController.popBackStack() },
                        viewModel = detailsViewModel
                    )
                    detailsViewModel.getDetails(details.recipeId)
                }
            }
        }
    }
}