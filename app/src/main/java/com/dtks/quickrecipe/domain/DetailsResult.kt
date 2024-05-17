package com.dtks.quickrecipe.domain

data class DetailsResult(
    val recipeDetails: RecipeDetails,
    val messageToShow: String? = null,
)