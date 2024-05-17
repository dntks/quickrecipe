package com.dtks.quickrecipe.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert


@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipe WHERE title LIKE :title")
    suspend fun getAllByTitle(title: String): List<RecipeDBEntity>

    @Query("SELECT * FROM recipe WHERE ingredients LIKE :ingredients")
    suspend fun getAllByIngredients(ingredients: String): List<RecipeDBEntity>

    @Upsert
    suspend fun upsertRecipeEntities(recipeEntities: List<RecipeDBEntity>)

    @Upsert
    suspend fun upsertRecipeEntity(recipeEntity: RecipeDBEntity)

    @Query("SELECT * FROM recipe WHERE id =:id")
    fun findById(id: Long): RecipeDBEntity

}
