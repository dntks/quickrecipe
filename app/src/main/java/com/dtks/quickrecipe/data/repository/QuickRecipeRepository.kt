package com.dtks.quickrecipe.data.repository

import com.dtks.quickrecipe.data.api.RemoteDataSource
import com.dtks.quickrecipe.data.api.model.RecipeDetailsApiEntity
import com.dtks.quickrecipe.data.api.model.SearchApiRequest
import com.dtks.quickrecipe.data.api.model.SearchRecipesResponse
import com.dtks.quickrecipe.data.local.RecipeDao
import com.dtks.quickrecipe.domain.DetailsResult
import com.dtks.quickrecipe.domain.EntityTransformer
import com.dtks.quickrecipe.domain.SearchResult
import com.dtks.quickrecipe.domain.SearchType
import javax.inject.Inject

/*
    The repository first tries to call the backend. If it fails for anz reason it falls back to get data
    from the local database. The remote data retrieved is always saved to the database first.
*/
class QuickRecipeRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val recipeDao: RecipeDao,
    private val entityTransformer: EntityTransformer,
) {

    // On success saves the data to DB, on error it retrieves the data from the DB
    suspend fun search(apiRequest: SearchApiRequest): SearchResult {
        return when (val remoteResponse = remoteSearch(apiRequest)) {
            is Success -> saveRecipesToDB(remoteResponse.response)
            is Error -> searchRecipesInDB(apiRequest, REMOTE_ERROR_MESSAGE)
        }
    }

    private suspend fun searchRecipesInDB(
        apiRequest: SearchApiRequest,
        errorMessage: String
    ): SearchResult {
        val searchPhrase = "%${apiRequest.searchPhrase}%"
        val dbEntities = when (apiRequest.searchType) {
            SearchType.BY_TITLE -> recipeDao.getAllByTitle(searchPhrase)
            SearchType.BY_INGREDIENT -> recipeDao.getAllByIngredients(searchPhrase)
        }
        return SearchResult(
            recipes = entityTransformer.transformRecipeDBEntitiesToDomainEntities(dbEntities),
            canLoadMore = false,
            messageToShow = errorMessage
        )
    }

    private suspend fun saveRecipesToDB(response: SearchRecipesResponse): SearchResult {
        val recipeEntities =
            entityTransformer.transformRecipeApiEntitiesToDBEntities(response.results)
        recipeDao.upsertRecipeEntities(recipeEntities)
        return SearchResult(
            recipes = entityTransformer.transformRecipeDBEntitiesToDomainEntities(recipeEntities),
            canLoadMore = response.totalResults > response.results.size + response.offset,
        )
    }

    private suspend fun remoteSearch(apiRequest: SearchApiRequest): RemoteResponse<SearchRecipesResponse> {
        return try {
            Success(remoteDataSource.search(apiRequest))
        } catch (exception: Exception) {
            Error(exception)
        }
    }

    // On success saves the data to DB, on error it retrieves the data from the DB
    suspend fun getRecipeDetails(id: Long): DetailsResult {
        return when (val remoteResponse = getRemoteRecipeDetails(id)) {
            is Success -> saveRecipeToDB(remoteResponse.response)
            is Error -> findRecipeInDB(id, REMOTE_ERROR_MESSAGE)
        }
    }

    private fun findRecipeInDB(id: Long, remoteErrorMessage: String): DetailsResult {
        val dbEntity = recipeDao.findById(id)
        return DetailsResult(
            recipeDetails = entityTransformer.transformRecipeDBEntityToDetails(dbEntity),
            messageToShow = remoteErrorMessage
        )
    }

    private suspend fun saveRecipeToDB(apiEntity: RecipeDetailsApiEntity): DetailsResult {
        val dbEntity = entityTransformer.transformRecipeDetailsApiEntityToDBEntity(apiEntity)
        recipeDao.upsertRecipeEntity(dbEntity)
        return DetailsResult(
            entityTransformer.transformRecipeDBEntityToDetails(dbEntity)
        )
    }

    private suspend fun getRemoteRecipeDetails(id: Long): RemoteResponse<RecipeDetailsApiEntity> {
        return try {
            Success(remoteDataSource.getRecipeDetails(id))
        } catch (exception: Exception) {
            Error(exception)
        }
    }

    companion object {
        const val REMOTE_ERROR_MESSAGE = "Couldn't load server response."
    }
}

sealed class RemoteResponse<T>

data class Success<T>(val response: T) : RemoteResponse<T>()
data class Error<T>(val throwable: Throwable) : RemoteResponse<T>()