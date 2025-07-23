package com.dtks.quickrecipe.data

import com.dtks.quickrecipe.data.api.model.IngredientApiEntity
import com.dtks.quickrecipe.data.api.model.RecipeApiEntity
import com.dtks.quickrecipe.data.api.model.RecipeDetailsApiEntity
import com.dtks.quickrecipe.data.local.RecipeDBEntity
import com.dtks.quickrecipe.data.repository.model.DetailsResult
import com.dtks.quickrecipe.data.repository.model.RecipeDetails
import com.dtks.quickrecipe.data.repository.model.RecipeDomainEntity
import com.dtks.quickrecipe.data.repository.model.SearchResult


val defaultRecipeDBEntity = RecipeDBEntity(
    id = 5,
    title = "Kevin's chili",
    image = "http://scrantonchilli.com",
    ingredients = "chili, love",
    instructions = "The trick is to undercook the onions.",
    urlToSource = "http://dunder-mifflin.com"
)
val secondRecipeDBEntity = RecipeDBEntity(
    id = 88,
    title = "Tiramisu",
    image = "http://italy.com",
    ingredients = "mascarpone, rum",
    instructions = "Put as much mascarpone as you can",
    urlToSource = "http://mascarpone.com"
)

val defaultRecipeDBEntityList = listOf(defaultRecipeDBEntity, secondRecipeDBEntity)
val reducedRecipeDBEntityList = listOf(
    defaultRecipeDBEntity.copy(
        instructions = null,
        urlToSource = null
    ),
    secondRecipeDBEntity.copy(
        instructions = null,
        urlToSource = null
    )
)

val defaultRecipeDetails = RecipeDetails(
    id = 5,
    title = "Kevin's chili",
    imageUrl = "http://scrantonchilli.com",
    ingredients = "chili, love",
    instructions = "The trick is to undercook the onions.",
    urlToSource = "http://dunder-mifflin.com"
)
val defaultRecipeApiEntity = RecipeApiEntity(
    id = 5,
    title = "Kevin's chili",
    image = "http://scrantonchilli.com",
    usedIngredients = listOf(
        IngredientApiEntity(
            id = 42,
            name = "chili"
        ),
        IngredientApiEntity(
            id = 33,
            name = "love"
        )
    ),
    imageType = "jpg",
)
val secondRecipeApiEntity = RecipeApiEntity(
    id = 88,
    title = "Tiramisu",
    image = "http://italy.com",
    usedIngredients = listOf(
        IngredientApiEntity(
            id = 42,
            name = "mascarpone"
        ),
        IngredientApiEntity(
            id = 33,
            name = "rum"
        )
    ),
    imageType = "jpg",
)
val defaultRecipeApiEntityList = listOf(defaultRecipeApiEntity, secondRecipeApiEntity)

val defaultRecipeDetailsApiEntity = RecipeDetailsApiEntity(
    id = 5,
    title = "Kevin's chili",
    image = "http://scrantonchilli.com",
    extendedIngredients = listOf(
        IngredientApiEntity(
            id = 42,
            name = "chili"
        ),
        IngredientApiEntity(
            id = 33,
            name = "love"
        )
    ),
    instructions = "The trick is to undercook the onions.",
    sourceUrl = "http://dunder-mifflin.com"
)
val defaultDomainEntity = RecipeDomainEntity(
    id = 5,
    title = "Kevin's chili",
    ingredients = "chili, love",
    imageUrl = "http://scrantonchilli.com",
)
val secondDomainEntity = RecipeDomainEntity(
    id = 88,
    title = "Tiramisu",
    ingredients = "mascarpone, rum",
    imageUrl = "http://italy.com",
)
val defaultDomainEntities = listOf(defaultDomainEntity, secondDomainEntity)

val defaultSearchResult = SearchResult(
    recipes = defaultDomainEntities,
    messageToShow = "interesting message",
    canLoadMore = false
)

val defaultDetailsResult = DetailsResult(
    recipeDetails = defaultRecipeDetails,
    messageToShow = "some message"
)