package com.dtks.quickrecipe.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

//Unfortunately didn't have time to add this functionality
@Entity(
    tableName = "favorite_recipe"
)
data class FavoriteRecipeEntity(
    @PrimaryKey val recipeId: Int,
    val isFavorite: Boolean = false,
)