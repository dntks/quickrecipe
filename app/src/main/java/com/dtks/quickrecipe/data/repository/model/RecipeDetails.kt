package com.dtks.quickrecipe.data.repository.model

data class RecipeDetails(
    val id: Long,
    val title: String,
    val imageUrl: String,
    val ingredients: String,
    val instructions: String,
    val urlToSource: String,
)