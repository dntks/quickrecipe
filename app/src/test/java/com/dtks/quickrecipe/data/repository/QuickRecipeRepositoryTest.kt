package com.dtks.quickrecipe.data.repository

import com.dtks.quickrecipe.data.api.RemoteDataSource
import com.dtks.quickrecipe.data.api.model.SearchApiRequest
import com.dtks.quickrecipe.data.api.model.SearchRecipesResponse
import com.dtks.quickrecipe.data.defaultDomainEntities
import com.dtks.quickrecipe.data.defaultRecipeApiEntityList
import com.dtks.quickrecipe.data.defaultRecipeDBEntity
import com.dtks.quickrecipe.data.defaultRecipeDBEntityList
import com.dtks.quickrecipe.data.defaultRecipeDetails
import com.dtks.quickrecipe.data.defaultRecipeDetailsApiEntity
import com.dtks.quickrecipe.data.local.RecipeDao
import com.dtks.quickrecipe.domain.EntityTransformer
import com.dtks.quickrecipe.domain.SearchResult
import com.dtks.quickrecipe.domain.SearchType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class QuickRecipeRepositoryTest {
    private val remoteDataSourceMock: RemoteDataSource = mockk(relaxed = true)
    private val recipeDaoMock: RecipeDao = mockk(relaxed = true)
    private val entityTransformer: EntityTransformer = mockk(relaxed = true)
    @OptIn(ExperimentalCoroutinesApi::class)
    private val repository: QuickRecipeRepository = QuickRecipeRepository(
        remoteDataSource = remoteDataSourceMock,
        recipeDao = recipeDaoMock,
        entityTransformer = entityTransformer,
        applicationScope = TestScope(),
        dispatcher = UnconfinedTestDispatcher()
    )

    @Before
    fun setup() {
        coEvery { remoteDataSourceMock.search(any()) } returns SearchRecipesResponse(
            results = defaultRecipeApiEntityList,
            offset = 0,
            number = 10,
            totalResults = 29
        )
        coEvery { remoteDataSourceMock.getRecipeDetails(any()) } returns defaultRecipeDetailsApiEntity
        coEvery { recipeDaoMock.findById(any()) } returns defaultRecipeDBEntity
        coEvery { recipeDaoMock.getAllByTitle(any()) } returns defaultRecipeDBEntityList
        coEvery { recipeDaoMock.getAllByIngredients(any()) } returns defaultRecipeDBEntityList
        coEvery { entityTransformer.transformRecipeDBEntityToDetails(any()) } returns defaultRecipeDetails
        coEvery { entityTransformer.transformRecipeDBEntitiesToDomainEntities(any()) } returns defaultDomainEntities
        coEvery { entityTransformer.transformRecipeApiEntitiesToDBEntities(any()) } returns defaultRecipeDBEntityList
        coEvery { entityTransformer.transformRecipeApiEntityToDBEntity(any()) } returns defaultRecipeDBEntity
        coEvery { entityTransformer.transformRecipeDetailsApiEntityToDBEntity(any()) } returns defaultRecipeDBEntity
    }

    @Test
    fun givenTotalResultsBiggerThanOffsetPlusNumberWhenSearchCalledThenSearchResultReturnedCorrectly() =
        runTest {

            coEvery { remoteDataSourceMock.search(any()) } returns SearchRecipesResponse(
                results = defaultRecipeApiEntityList,
                offset = 30,
                number = 10,
                totalResults = 29
            )
            val expectedResult = SearchResult(
                recipes = defaultDomainEntities,
                canLoadMore = false
            )

            val searchResult = repository.search(
                SearchApiRequest(
                    "aba",
                    searchType = SearchType.BY_INGREDIENT,
                    offset = 10
                )
            )


            Assert.assertEquals(expectedResult, searchResult)
        }

    @Test
    fun givenRemoteSearchSuccessfulWhenSearchCalledThenSearchResultReturnedCorrectly() =
        runBlocking {
            val expectedResult = SearchResult(
                recipes = defaultDomainEntities,
                canLoadMore = true
            )

            val searchResult = repository.search(
                SearchApiRequest(
                    "aba",
                    searchType = SearchType.BY_INGREDIENT,
                    offset = 10
                )
            )


            Assert.assertEquals(expectedResult, searchResult)
        }

    @Test
    fun givenRemoteSearchSuccessfulWhenSearchIsCalledThenDBDataNotRetrieved() =
        runBlocking {

            val apiRequest = SearchApiRequest(
                "aba",
                searchType = SearchType.BY_INGREDIENT,
                offset = 10
            )
            repository.search(apiRequest)

            coVerify { remoteDataSourceMock.search(apiRequest) }
            coVerify { entityTransformer.transformRecipeApiEntitiesToDBEntities(any()) }
            coVerify { recipeDaoMock.upsertRecipeEntities(defaultRecipeDBEntityList) }
            coVerify {
                entityTransformer.transformRecipeDBEntitiesToDomainEntities(
                    defaultRecipeDBEntityList
                )
            }

        }

    @Test
    fun givenRemoteSearchThrowsErrWhenSearchIsCalledThenDBDataIsRetrievedByIngredient() =
        runBlocking {
            coEvery { remoteDataSourceMock.search(any()) } throws Exception("bombadil")

            val expectedResult = SearchResult(
                recipes = defaultDomainEntities,
                canLoadMore = false,
                messageToShow = "Couldn't load server response."
            )
            val apiRequest = SearchApiRequest(
                "tom",
                searchType = SearchType.BY_INGREDIENT,
                offset = 10
            )
            val actualResult = repository.search(apiRequest)

            coVerify { recipeDaoMock.getAllByIngredients("%tom%") }
            coVerify {
                entityTransformer.transformRecipeDBEntitiesToDomainEntities(
                    defaultRecipeDBEntityList
                )
            }
            Assert.assertEquals(expectedResult, actualResult)
        }

    @Test
    fun givenRemoteSearchThrowsErrorWhenSearchIsCalledThenDBDataIsRetrievedByTitle() =
        runBlocking {
            coEvery { remoteDataSourceMock.search(any()) } throws Exception("bombadil")

            val expectedResult = SearchResult(
                recipes = defaultDomainEntities,
                canLoadMore = false,
                messageToShow = "Couldn't load server response."
            )
            val apiRequest = SearchApiRequest(
                "tom",
                searchType = SearchType.BY_TITLE,
                offset = 10
            )
            val actualResult = repository.search(apiRequest)

            coVerify { recipeDaoMock.getAllByTitle("%tom%") }
            coVerify {
                entityTransformer.transformRecipeDBEntitiesToDomainEntities(
                    defaultRecipeDBEntityList
                )
            }
            Assert.assertEquals(expectedResult, actualResult)
        }

    @Test
    fun givenGetRemoteRecipeDetailsReturnsValueWhenGetRecipeDetailsThenDBDataNotCalled() =
        runBlocking {
            coEvery { remoteDataSourceMock.search(any()) } throws Exception("bombadil")

            val expectedResult = SearchResult(
                recipes = defaultDomainEntities,
                canLoadMore = false,
                messageToShow = "Couldn't load server response."
            )
            val apiRequest = SearchApiRequest(
                "tom",
                searchType = SearchType.BY_TITLE,
                offset = 10
            )
            val actualResult = repository.search(apiRequest)

            coVerify { recipeDaoMock.getAllByTitle("%tom%") }
            coVerify {
                entityTransformer.transformRecipeDBEntitiesToDomainEntities(
                    defaultRecipeDBEntityList
                )
            }
            Assert.assertEquals(expectedResult, actualResult)
        }

    @Test
    fun givenGetRemoteRecipeDetailsThrowsExceptionWhenGetRecipeDetailsThenDBDataIsRetrieved() =
        runBlocking {
            coEvery { remoteDataSourceMock.getRecipeDetails(any()) } throws Exception("bombadil")

            repository.getRecipeDetails(7)

            coVerify { recipeDaoMock.findById(7) }
            coVerify { entityTransformer.transformRecipeDBEntityToDetails(defaultRecipeDBEntity) }
        }
}