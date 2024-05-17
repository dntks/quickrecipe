package com.dtks.quickrecipe.viewmodel.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dtks.quickrecipe.di.DefaultDispatcher
import com.dtks.quickrecipe.domain.usecases.RecipeDetailsUseCase
import com.dtks.quickrecipe.ui.navigation.QuickRecipeDestinationsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val recipeDetailsUseCase: RecipeDetailsUseCase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val recipeId: String = savedStateHandle[QuickRecipeDestinationsArgs.RECIPE_ID_ARG]!!

    private val _detailsFlow: MutableStateFlow<DetailScreenState> = MutableStateFlow(Loading)
    val detailsFlow = _detailsFlow.asStateFlow()

    init {
        recipeId.toLongOrNull()?.let { id ->
            getDetails(id)
        }
    }

    private fun getDetails(id: Long) {
        viewModelScope.launch(dispatcher) {
            try {
                val detailsResponse = recipeDetailsUseCase(id)
                val detailsState = RecipeDetailsLoaded(
                    details = detailsResponse.recipeDetails,
                    message = detailsResponse.messageToShow
                )
                _detailsFlow.value = detailsState
            } catch (exception: Exception) {
                _detailsFlow.value = DetailsError(exception.message)
            }
        }
    }
}
