package com.dtks.quickrecipe.domain.usecases

import com.dtks.quickrecipe.data.repository.QuickRecipeRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class RecipeDetailsUseCaseTest {

    private val repositoryMock = mockk<QuickRecipeRepository>(relaxed = true)
    private val useCase = RecipeDetailsUseCase(repositoryMock)

    @Test
    fun whenUseCaseInvokedThenRepositoryIsCalled() = runBlocking {
        useCase.invoke(5)

        coVerify { repositoryMock.getRecipeDetails(5) }
    }
}