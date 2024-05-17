package com.dtks.quickrecipe.domain.usecases

import com.dtks.quickrecipe.data.api.model.SearchApiRequest
import com.dtks.quickrecipe.data.repository.QuickRecipeRepository
import com.dtks.quickrecipe.domain.SearchResult
import com.dtks.quickrecipe.viewmodel.overview.SearchState
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: QuickRecipeRepository
) {

    suspend operator fun invoke(searchState: SearchState): SearchResult {
        return repository.search(
            SearchApiRequest(
                searchPhrase = searchState.searchText,
                searchType = searchState.searchType,
                offset = searchState.offset
            )
        )
    }
}