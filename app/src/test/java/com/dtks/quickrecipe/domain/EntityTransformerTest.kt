package com.dtks.quickrecipe.domain

import com.dtks.quickrecipe.data.defaultDomainEntities
import com.dtks.quickrecipe.data.defaultRecipeApiEntity
import com.dtks.quickrecipe.data.defaultRecipeApiEntityList
import com.dtks.quickrecipe.data.defaultRecipeDBEntity
import com.dtks.quickrecipe.data.defaultRecipeDBEntityList
import com.dtks.quickrecipe.data.defaultRecipeDetails
import com.dtks.quickrecipe.data.defaultRecipeDetailsApiEntity
import com.dtks.quickrecipe.data.reducedRecipeDBEntityList
import com.dtks.quickrecipe.data.repository.EntityTransformer
import org.junit.Assert
import org.junit.Test

class EntityTransformerTest {

    private val transformer = EntityTransformer()

    @Test
    fun transformRecipeApiEntityToDbEntityIsCorrect() {
        val expectedDBEntity = defaultRecipeDBEntity.copy(
            instructions = null,
            urlToSource = null
        )

        val actualDbEntity =
            transformer.transformRecipeApiEntityToDBEntity(defaultRecipeApiEntity)

        Assert.assertEquals(expectedDBEntity, actualDbEntity)
    }

    @Test
    fun transformRecipeDetailsApiEntityToDbEntityIsCorrect() {
        val expectedDBEntity = defaultRecipeDBEntity

        val actualDbEntity =
            transformer.transformRecipeDetailsApiEntityToDBEntity(defaultRecipeDetailsApiEntity)

        Assert.assertEquals(expectedDBEntity, actualDbEntity)
    }

    @Test
    fun transformRecipeDBEntityToRecipeDetailsIsCorrect() {
        val expectedRecipeDetails = defaultRecipeDetails

        val actualRecipeDetails =
            transformer.transformRecipeDBEntityToDetails(defaultRecipeDBEntity)

        Assert.assertEquals(expectedRecipeDetails, actualRecipeDetails)
    }

    @Test
    fun transformRecipeApiEntitiesToDBEntitiesIsCorrect() {
        val expectedDBEntities = reducedRecipeDBEntityList

        val actualDBEntities =
            transformer.transformRecipeApiEntitiesToDBEntities(defaultRecipeApiEntityList)

        Assert.assertEquals(expectedDBEntities, actualDBEntities)
    }

    @Test
    fun transformRecipeDBEntitiesToDomainEntitiesIsCorrect() {
        val expectedDomainEntities = defaultDomainEntities

        val actualDomainEntities =
            transformer.transformRecipeDBEntitiesToDomainEntities(defaultRecipeDBEntityList)

        Assert.assertEquals(expectedDomainEntities, actualDomainEntities)
    }
}