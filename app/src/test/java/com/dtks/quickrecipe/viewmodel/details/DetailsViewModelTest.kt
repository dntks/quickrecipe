package com.dtks.quickrecipe.viewmodel.details

import androidx.lifecycle.SavedStateHandle
import com.dtks.quickrecipe.data.defaultDetailsResult
import com.dtks.quickrecipe.data.defaultRecipeDetails
import com.dtks.quickrecipe.domain.usecases.RecipeDetailsUseCase
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

class DetailsViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private var testDispatcher = UnconfinedTestDispatcher()
    private var testScope = TestScope(testDispatcher)
    private lateinit var detailsViewModel: DetailsViewModel
    private val stateHandleMock = mockk<SavedStateHandle>(relaxed = true)
    private val recipeDetailsUseCaseMock: RecipeDetailsUseCase = mockk(relaxed = true)


    @Before
    fun setup() {
        coEvery { stateHandleMock.get<String>(any()) } returns "4"
        coEvery { recipeDetailsUseCaseMock.invoke(any()) } returns defaultDetailsResult
    }

    @Test
    fun whenInitializedDetailsViewModelCallsRecipeDetailsUseCase() = testScope.runTest {
        detailsViewModel =
            DetailsViewModel(stateHandleMock, recipeDetailsUseCaseMock, testDispatcher)

        coVerify { recipeDetailsUseCaseMock.invoke(4) }
    }

    @Test
    fun whenInitializedDetailsViewModelSetsStateToRecipeDetailsLoaded() = testScope.runTest {
        val expectedState = RecipeDetailsLoaded(details = defaultRecipeDetails)

        detailsViewModel =
            DetailsViewModel(stateHandleMock, recipeDetailsUseCaseMock, testDispatcher)

        val actualState = detailsViewModel.detailsFlow.first()
        Assert.assertEquals(expectedState, actualState)
    }

    @Test
    fun givenUseCaseThrowsErrorWhenInitThenDetailsFlowStateIsSetToError() = testScope.runTest {
        val expectedState = DetailsError("oho")
        coEvery { recipeDetailsUseCaseMock.invoke(any()) } throws Exception("oho")

        detailsViewModel =
            DetailsViewModel(stateHandleMock, recipeDetailsUseCaseMock, testDispatcher)

        val actualState = detailsViewModel.detailsFlow.first()
        Assert.assertEquals(expectedState, actualState)
    }

}