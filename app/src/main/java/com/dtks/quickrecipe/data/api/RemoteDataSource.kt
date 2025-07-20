package com.dtks.quickrecipe.data.api

import com.dtks.quickrecipe.data.api.model.RecipeDetailsApiEntity
import com.dtks.quickrecipe.data.api.model.SearchApiRequest
import com.dtks.quickrecipe.data.api.model.SearchRecipesResponse
import com.dtks.quickrecipe.di.IoDispatcher
import com.dtks.quickrecipe.domain.SearchType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val recipeApi: RecipeApi,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    suspend fun search(
        searchRequest: SearchApiRequest
    ): SearchRecipesResponse = withContext(ioDispatcher){
        when (searchRequest.searchType) {
            SearchType.BY_TITLE -> recipeApi.searchRecipesByTitle(
                query = searchRequest.searchPhrase,
                offset = searchRequest.offset
            )

            SearchType.BY_INGREDIENT -> recipeApi.searchRecipesByIngredient(
                includeIngredients = searchRequest.searchPhrase,
                offset = searchRequest.offset
            )
        }
    }

    suspend fun getRecipeDetails(
        recipeId: Long
    ): RecipeDetailsApiEntity = withContext(ioDispatcher){
        recipeApi.getRecipeDetails(recipeId = recipeId)
    }
}