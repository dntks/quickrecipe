package com.dtks.quickrecipe.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

//Unfortunately didn't have time to add this functionality
@Dao
interface FavoriteRecipeDao {

    @Query("SELECT * FROM favorite_recipe WHERE recipeId=:id ")
    suspend fun getFavoriteById(id: Int): FavoriteRecipeEntity?

    @Query("SELECT * FROM favorite_recipe WHERE isFavorite = 1 ORDER BY recipeId")
    fun favoritesFlow(): Flow<List<FavoriteRecipeEntity>>

    @Upsert
    suspend fun update(entity: FavoriteRecipeEntity)
}