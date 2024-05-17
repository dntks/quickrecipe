package com.dtks.quickrecipe.data.api.model

data class RecipeApiEntity(
    val id: Long,
    val title: String,
    val image: String,
    val imageType: String,
    val usedIngredients: List<IngredientApiEntity>?,
)