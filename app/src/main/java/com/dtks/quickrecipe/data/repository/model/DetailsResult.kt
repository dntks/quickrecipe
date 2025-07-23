package com.dtks.quickrecipe.data.repository.model

data class DetailsResult(
    val recipeDetails: RecipeDetails,
    val messageToShow: String? = null,
)