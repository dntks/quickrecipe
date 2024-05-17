package com.dtks.quickrecipe.data.api.model

data class SearchRecipesResponse(
    val results: List<RecipeApiEntity>,
    val offset: Int,
    val number: Int,
    val totalResults: Int
)