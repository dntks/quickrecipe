package com.dtks.quickrecipe.domain.usecases

import com.dtks.quickrecipe.data.repository.QuickRecipeRepository
import com.dtks.quickrecipe.data.repository.model.DetailsResult
import javax.inject.Inject

class RecipeDetailsUseCase @Inject constructor(
    private val repository: QuickRecipeRepository
) {

    suspend operator fun invoke(id: Long): DetailsResult {
        return repository.getRecipeDetails(id)
    }
}