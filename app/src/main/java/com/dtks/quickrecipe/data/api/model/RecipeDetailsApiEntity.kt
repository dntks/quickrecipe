package com.dtks.quickrecipe.data.api.model

data class RecipeDetailsApiEntity(
    val id: Long,
    val title: String,
    val image: String,
    val sourceUrl: String,
    val extendedIngredients: List<IngredientApiEntity>?,
    val instructions: String?,
)