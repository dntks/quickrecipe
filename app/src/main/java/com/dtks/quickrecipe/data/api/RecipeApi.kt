package com.dtks.quickrecipe.data.api

import com.dtks.quickrecipe.BuildConfig.API_KEY
import com.dtks.quickrecipe.data.api.model.RecipeDetailsApiEntity
import com.dtks.quickrecipe.data.api.model.SearchRecipesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {

    @GET("complexSearch")
    suspend fun searchRecipesByTitle(
        @Query("apiKey")
        apiKey: String = API_KEY,
        @Query("offset")
        offset: Int = 0,
        @Query("number")
        number: Int = ELEMENTS_PER_REQUEST,
        @Query("query")
        query: String,
        @Query("fillIngredients")
        fillIngredients: Boolean = true
    ): SearchRecipesResponse

    @GET("complexSearch")
    suspend fun searchRecipesByIngredient(
        @Query("apiKey")
        apiKey: String = API_KEY,
        @Query("offset")
        offset: Int = 0,
        @Query("number")
        number: Int = ELEMENTS_PER_REQUEST,
        @Query("includeIngredients")
        includeIngredients: String,
        @Query("fillIngredients")
        fillIngredients: Boolean = true
    ): SearchRecipesResponse

    @GET("{id}/information")
    suspend fun getRecipeDetails(
        @Path("id")
        recipeId: Long = 0,
        @Query("apiKey")
        apiKey: String = API_KEY,
    ): RecipeDetailsApiEntity

    companion object {
        const val API_KEY = "225994409205428ab8859ea0a87eeaf0"
        const val ELEMENTS_PER_REQUEST = 40
    }
}