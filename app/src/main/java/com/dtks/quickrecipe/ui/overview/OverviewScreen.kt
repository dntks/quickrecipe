package com.dtks.quickrecipe.ui.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dtks.quickrecipe.R
import com.dtks.quickrecipe.domain.RecipeDomainEntity
import com.dtks.quickrecipe.ui.common.LoadingContent
import com.dtks.quickrecipe.ui.common.NoResultsScreen
import com.dtks.quickrecipe.ui.theme.Typography
import com.dtks.quickrecipe.viewmodel.overview.OverviewError
import com.dtks.quickrecipe.viewmodel.overview.OverviewScreenLoading
import com.dtks.quickrecipe.viewmodel.overview.OverviewViewModel
import com.dtks.quickrecipe.viewmodel.overview.RecipesLoaded
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverviewScreen(
    viewModel: OverviewViewModel = hiltViewModel(),
    onRecipeClick: (RecipeDomainEntity) -> Unit,
) {
    val recipeListState by viewModel.recipeListState.collectAsStateWithLifecycle()
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.recipes),
                        style = Typography.titleLarge
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary),
            )
        },
        floatingActionButton = {}
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            RecipeSearchBar(
                searchState = searchState,
                onSearchTextChange = viewModel::onSearchTextChange,
                onSearchTypeChange = viewModel::onSearchTypeChange
            )
            LoadingContent(
                loading = recipeListState is OverviewScreenLoading,
                empty = recipeListState.stateData.isNullOrEmpty(),
                error = recipeListState is OverviewError,
                emptyContent = { NoResultsScreen(R.string.no_recipes_found) },
                errorContent = {
                    NoResultsScreen(R.string.details_error)
                }
            ) {
                val recipes = recipeListState.stateData
                RecipeList(
                    recipes = recipes.orEmpty(),
                    uiState = recipeListState,
                    onClick = onRecipeClick,
                ) { viewModel::loadMore }
            }
        }
        (recipeListState as? RecipesLoaded)?.message?.let {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = it
                )
            }
        }
    }

}