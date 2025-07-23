package com.dtks.quickrecipe.viewmodel.details

import com.dtks.quickrecipe.data.repository.model.RecipeDetails

sealed class DetailScreenState

data object Loading : DetailScreenState()
data class RecipeDetailsLoaded(val details: RecipeDetails, val message: String?) :
    DetailScreenState()
data class DetailsError(val message: String?) : DetailScreenState()