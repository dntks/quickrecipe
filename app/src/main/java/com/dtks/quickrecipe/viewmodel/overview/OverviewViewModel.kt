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

    private val _uiState =
        MutableStateFlow<OverviewScreenState<List<RecipeDomainEntity>>>(OverviewScreenLoading())
    val uiState = _uiState.asStateFlow()

    init {
        searchRecipes()
    }

    private fun searchRecipes() {
        _uiState.value = OverviewScreenLoading()
        viewModelScope.launch(dispatcher) {
            try {
                val searchResult = searchUseCase(_searchState.value)
                _searchState.value = _searchState.value.copy(canLoadMore = searchResult.canLoadMore)
                _uiState.value = RecipesLoaded(
                    data = searchResult.recipes
                )
            } catch (exception: Exception) {
                _uiState.value = OverviewError()
            }
        }
    }

    private fun loadMoreRecipes() {
        _uiState.value = OverviewScreenLoading(_uiState.value.stateData)
        viewModelScope.launch(dispatcher) {
            try {
                val searchResult = searchUseCase(_searchState.value)
                _searchState.value = _searchState.value.copy(canLoadMore = searchResult.canLoadMore)
                val currentResults = _uiState.value.stateData.orEmpty()
                _uiState.value = RecipesLoaded(
                    data = currentResults + searchResult.recipes
                )
            } catch (exception: Exception) {
                _uiState.value = OverviewError(_uiState.value.stateData)
            }
        }
    }

    fun onSearchTextChange(text: String) {
        _searchState.value = _searchState.value.copy(
            searchText = text,
            offset = 0,
            canLoadMore = true
        )
        _uiState.value = OverviewScreenLoading(
            data = emptyList()
        )
        searchRecipes()
    }

    fun onSearchTypeChange(type: SearchType) {
        _searchState.value = _searchState.value.copy(
            searchType = type,
            offset = 0,
            canLoadMore = true
        )
        _uiState.value = OverviewScreenLoading(
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
