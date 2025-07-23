package com.dtks.quickrecipe.domain.usecases

import com.dtks.quickrecipe.data.api.model.SearchApiRequest
import com.dtks.quickrecipe.data.repository.QuickRecipeRepository
import com.dtks.quickrecipe.data.repository.model.SearchType
import com.dtks.quickrecipe.viewmodel.overview.SearchState
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SearchUseCaseTest {

    private val repositoryMock = mockk<QuickRecipeRepository>(relaxed = true)
    private val useCase = SearchUseCase(repositoryMock)

    @Test
    fun whenUseCaseInvokedThenRepositoryIsCalled() = runBlocking {
        useCase.invoke(
            SearchState(
                searchType = SearchType.BY_TITLE,
                searchText = "frodo",
                offset = 2
            )
        )

        coVerify {
            repositoryMock.search(
                SearchApiRequest(
                    searchPhrase = "frodo",
                    SearchType.BY_TITLE,
                    offset = 2
                )
            )
        }
    }
}