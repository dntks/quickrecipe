package com.dtks.quickrecipe.data.api

import com.dtks.quickrecipe.data.api.model.SearchApiRequest
import com.dtks.quickrecipe.data.api.model.SearchRecipesResponse
import com.dtks.quickrecipe.data.defaultRecipeApiEntityList
import com.dtks.quickrecipe.data.repository.model.SearchType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RemoteDataSourceTest {

    private val recipeApi = mockk<RecipeApi>()
    private val remoteDataSource = RemoteDataSource(recipeApi)

    @Before
    fun setup() {
        val searchRecipesResponse = SearchRecipesResponse(
            results = defaultRecipeApiEntityList,
            offset = 10,
            number = 20,
            totalResults = 10
        )
        coEvery {
            recipeApi.searchRecipesByIngredient(
                any(),
                any(),
                any(),
                any()
            )
        } returns searchRecipesResponse
        coEvery {
            recipeApi.searchRecipesByTitle(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns searchRecipesResponse
    }

    @Test
    fun givenSearchTypeByIngredientThenSearchRecipesByIngredientIsCalled() = runBlocking {
        remoteDataSource.search(
            SearchApiRequest(
                searchPhrase = "bombadil",
                searchType = SearchType.BY_INGREDIENT,
                offset = 10
            )
        )

        coVerify {
            recipeApi.searchRecipesByIngredient(
                offset = 10,
                includeIngredients = "bombadil"
            )
        }
    }

    @Test
    fun givenSearchTypeByTitleThenSearchByTitleIsCalled() = runBlocking {
        remoteDataSource.search(
            SearchApiRequest(
                searchPhrase = "bombadil",
                searchType = SearchType.BY_TITLE,
                offset = 10
            )
        )

        coVerify {
            recipeApi.searchRecipesByTitle(
                offset = 10,
                query = "bombadil"
            )
        }
    }
}