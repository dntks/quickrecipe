package com.dtks.quickrecipe.viewmodel.overview

import com.dtks.quickrecipe.data.defaultDomainEntities
import com.dtks.quickrecipe.data.defaultSearchResult
import com.dtks.quickrecipe.domain.RecipeDomainEntity
import com.dtks.quickrecipe.domain.SearchType.BY_INGREDIENT
import com.dtks.quickrecipe.domain.usecases.SearchUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class OverviewViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private var testDispatcher = UnconfinedTestDispatcher()
    private var testScope = TestScope(testDispatcher)
    private lateinit var overviewViewModel: OverviewViewModel
    private val searchUseCaseMock: SearchUseCase = mockk(relaxed = true)

    @Before
    fun setup() {
        coEvery { searchUseCaseMock.invoke(any()) } returns defaultSearchResult
    }

    @Test
    fun initCallsSearchUseCaseLoading() = testScope.runTest {
        val defaultSearchState = SearchState()

        overviewViewModel = OverviewViewModel(
            searchUseCase = searchUseCaseMock,
            dispatcher = testDispatcher
        )

        coVerify { searchUseCaseMock.invoke(defaultSearchState) }
    }

    @Test
    fun initSetsStateToRecipesLoaded() = testScope.runTest {
        val expectedState =
            RecipesLoaded(data = defaultDomainEntities, message = "interesting message")

        overviewViewModel = OverviewViewModel(
            searchUseCase = searchUseCaseMock,
            dispatcher = testDispatcher
        )

        val state = overviewViewModel.uiState.first()

        Assert.assertEquals(expectedState, state)
    }

    @Test
    fun givenUseCaseThrowsErrorWhenViewModelInitThenStateIsError() = testScope.runTest {
        coEvery { searchUseCaseMock.invoke(any()) } throws Exception("hop")
        val expectedState = OverviewError<List<RecipeDomainEntity>>()

        overviewViewModel = OverviewViewModel(
            searchUseCase = searchUseCaseMock,
            dispatcher = testDispatcher
        )

        val state = overviewViewModel.uiState.first()

        Assert.assertEquals(expectedState, state)
    }

    @Test
    fun onSearchTextChangeCallsUseCaseWithCorrectParams() = testScope.runTest {
        val expectedSearchState = SearchState(searchText = "opo")

        overviewViewModel = OverviewViewModel(
            searchUseCase = searchUseCaseMock,
            dispatcher = testDispatcher
        )

        overviewViewModel.onSearchTextChange("opo")

        coVerify { searchUseCaseMock.invoke(expectedSearchState) }
    }

    @Test
    fun onSearchTypeChangeCallsUseCaseWithCorrectParams() = testScope.runTest {
        val expectedSearchType = SearchState(searchType = BY_INGREDIENT)

        overviewViewModel = OverviewViewModel(
            searchUseCase = searchUseCaseMock,
            dispatcher = testDispatcher
        )

        overviewViewModel.onSearchTypeChange(BY_INGREDIENT)

        coVerify { searchUseCaseMock.invoke(expectedSearchType) }
    }

    @Test
    fun givenCanLoadMoreFalseWhenLoadMoreIsCalledUseCaseIsNotCalled() = testScope.runTest {
        val expectedSearchType = SearchState(offset = 40)

        overviewViewModel = OverviewViewModel(
            searchUseCase = searchUseCaseMock,
            dispatcher = testDispatcher
        )

        overviewViewModel.loadMore()

        coVerify(exactly = 0) { searchUseCaseMock.invoke(expectedSearchType) }
    }

    @Test
    fun givenCanLoadMoreTrueWhenLoadMoreIsCalledUseCaseIsNotCalled() = testScope.runTest {
        coEvery { searchUseCaseMock.invoke(any()) } returns defaultSearchResult.copy(canLoadMore = true)
        val expectedSearchType = SearchState(offset = 40)

        overviewViewModel = OverviewViewModel(
            searchUseCase = searchUseCaseMock,
            dispatcher = testDispatcher
        )

        overviewViewModel.loadMore()

        coVerify { searchUseCaseMock.invoke(expectedSearchType) }
    }
}