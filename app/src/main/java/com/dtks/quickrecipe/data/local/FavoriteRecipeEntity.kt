package com.dtks.quickrecipe.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_recipe"
)
data class FavoriteRecipeEntity(
    @PrimaryKey val recipeId: Int,
    val isFavorite: Boolean = false,
)