package com.dtks.quickrecipe.viewmodel.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dtks.quickrecipe.data.api.RecipeApi
import com.dtks.quickrecipe.di.DefaultDispatcher
import com.dtks.quickrecipe.domain.RecipeDomainEntity
import com.dtks.quickrecipe.domain.SearchType
import com.dtks.quickrecipe.domain.usecases.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    val searchUseCase: SearchUseCase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _searchState = MutableStateFlow(SearchState())
    val searchState = _searchState.asStateFlow()

    private val _recipeListState =
        MutableStateFlow<OverviewScreenState<List<RecipeDomainEntity>>>(OverviewScreenLoading())
    val recipeListState = _recipeListState.asStateFlow()

    init {
        searchRecipes()
    }

    /*
    To be called when search params changed and offset is reset
     */
    private fun searchRecipes() {
        _recipeListState.value = OverviewScreenLoading()
        viewModelScope.launch(dispatcher) {
            try {
                val searchResult = searchUseCase(_searchState.value)
                _searchState.value = _searchState.value.copy(canLoadMore = searchResult.canLoadMore)
                _recipeListState.value = RecipesLoaded(
                    data = searchResult.recipes,
                    message = searchResult.messageToShow
                )
            } catch (exception: Exception) {
                _recipeListState.value = OverviewError()
            }
        }
    }

    /*
    To be called when paginating. The new results are merged with the current ones
     */
    private fun loadMoreRecipes() {
        _recipeListState.value = OverviewScreenLoading(_recipeListState.value.stateData)
        viewModelScope.launch {
            try {
                val searchResult = searchUseCase(_searchState.value)
                _searchState.value = _searchState.value.copy(canLoadMore = searchResult.canLoadMore)
                val currentResults = _recipeListState.value.stateData.orEmpty()
                _recipeListState.value = RecipesLoaded(
                    data = currentResults + searchResult.recipes,
                    message = searchResult.messageToShow
                )
            } catch (exception: Exception) {
                _recipeListState.value = OverviewError(_recipeListState.value.stateData)
            }
        }
    }

    //updating the searchstate with the search text, the start a new search with 0 offset
    fun onSearchTextChange(text: String) {
        _searchState.value = _searchState.value.copy(
            searchText = text,
            offset = 0,
            canLoadMore = true
        )
        _recipeListState.value = OverviewScreenLoading(
            data = emptyList()
        )
        searchRecipes()
    }

    //updating the searchstate with the type, the start a new search with 0 offset
    fun onSearchTypeChange(type: SearchType) {
        _searchState.value = _searchState.value.copy(
            searchType = type,
            offset = 0,
            canLoadMore = true
        )
        _recipeListState.value = OverviewScreenLoading(
            data = emptyList()
        )
        searchRecipes()
    }

    // paging solved with the 'canLoadMore' property
    fun loadMore() {
        val currentSearchState = _searchState.value
        if (currentSearchState.canLoadMore) {
            _searchState.value = currentSearchState.copy(
                offset = currentSearchState.offset + RecipeApi.ELEMENTS_PER_REQUEST,
            )
            loadMoreRecipes()
        }
    }
}
