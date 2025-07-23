package com.dtks.quickrecipe.ui.viewmodel.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dtks.quickrecipe.di.DefaultDispatcher
import com.dtks.quickrecipe.domain.usecases.RecipeDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    val recipeDetailsUseCase: RecipeDetailsUseCase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _detailsFlow: MutableStateFlow<DetailScreenState> = MutableStateFlow(Loading)
    val detailsFlow = _detailsFlow.asStateFlow()

     fun getDetails(id: Long) {
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
