package com.dtks.quickrecipe.domain

import com.dtks.quickrecipe.data.api.model.RecipeApiEntity
import com.dtks.quickrecipe.data.api.model.RecipeDetailsApiEntity
import com.dtks.quickrecipe.data.local.RecipeDBEntity
import javax.inject.Inject


// Transforming entities between the different layers.
// For convenience there's only one class, but it needs to be separated if it'd grow too big.
class EntityTransformer @Inject constructor() {

    fun transformRecipeDBEntitiesToDomainEntities(recipeDbEntityList: List<RecipeDBEntity>): List<RecipeDomainEntity> {
        return recipeDbEntityList.map {
            RecipeDomainEntity(
                id = it.id,
                title = it.title,
                imageUrl = it.image,
                ingredients = it.ingredients
            )
        }
    }

    fun transformRecipeApiEntitiesToDBEntities(recipeApiEntityList: List<RecipeApiEntity>): List<RecipeDBEntity> {
        return recipeApiEntityList.map { transformRecipeApiEntityToDBEntity(it) }
    }

    fun transformRecipeApiEntityToDBEntity(recipeApiEntity: RecipeApiEntity): RecipeDBEntity {
        return RecipeDBEntity(
            id = recipeApiEntity.id,
            title = recipeApiEntity.title,
            image = recipeApiEntity.image,
            ingredients = recipeApiEntity.usedIngredients.orEmpty().joinToString { it.name },
        )
    }

    fun transformRecipeDetailsApiEntityToDBEntity(recipeDetailsApiEntity: RecipeDetailsApiEntity): RecipeDBEntity {
        return RecipeDBEntity(
            id = recipeDetailsApiEntity.id,
            title = recipeDetailsApiEntity.title,
            image = recipeDetailsApiEntity.image,
            ingredients = recipeDetailsApiEntity.extendedIngredients.orEmpty()
                .joinToString { it.name },
            instructions = recipeDetailsApiEntity.instructions,
            urlToSource = recipeDetailsApiEntity.sourceUrl
        )
    }

    fun transformRecipeDBEntityToDetails(dbEntity: RecipeDBEntity): RecipeDetails {
        return RecipeDetails(
            id = dbEntity.id,
            title = dbEntity.title,
            imageUrl = dbEntity.image,
            ingredients = dbEntity.ingredients,
            instructions = dbEntity.instructions ?: "",
            urlToSource = dbEntity.urlToSource ?: "",
        )
    }
}
