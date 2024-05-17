package com.dtks.quickrecipe.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "recipe"
)
data class RecipeDBEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val image: String,
    val ingredients: String,
    val instructions: String? = null,
    val urlToSource: String? = null,
)