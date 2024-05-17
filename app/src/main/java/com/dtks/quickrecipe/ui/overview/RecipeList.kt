package com.dtks.quickrecipe.ui.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.dtks.quickrecipe.R
import com.dtks.quickrecipe.domain.RecipeDomainEntity
import com.dtks.quickrecipe.viewmodel.overview.OverviewScreenLoading
import com.dtks.quickrecipe.viewmodel.overview.OverviewScreenState

@Composable
fun RecipeList(
    recipes: List<RecipeDomainEntity>,
    uiState: OverviewScreenState<List<RecipeDomainEntity>>,
    onClick: (RecipeDomainEntity) -> Unit,
    loadMoreItems: () -> Unit,
) {
    val genericPadding = dimensionResource(id = R.dimen.generic_padding)
    val isOverviewScreenLoading = uiState is OverviewScreenLoading
    LazyColumn(
        contentPadding = PaddingValues(
            start = genericPadding,
            end = genericPadding,
            top = genericPadding,
            bottom = dimensionResource(id = R.dimen.extraBottom),
        ),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.grid_padding)),
        modifier = Modifier.fillMaxSize(),

        ) {
        items(recipes.size) { recipeIndex ->
            if (isOverviewScreenLoading.not() && recipeIndex > recipes.size - 4) {
                loadMoreItems()
            }
            RecipeCard(recipes[recipeIndex], onClick)
        }
    }
}
