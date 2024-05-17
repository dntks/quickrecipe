package com.dtks.quickrecipe.viewmodel.details

import com.dtks.quickrecipe.domain.RecipeDetails

sealed class DetailScreenState

data object Loading : DetailScreenState()
data class RecipeDetailsLoaded(val details: RecipeDetails) : DetailScreenState()
data class DetailsError(val message: String?) : DetailScreenState()