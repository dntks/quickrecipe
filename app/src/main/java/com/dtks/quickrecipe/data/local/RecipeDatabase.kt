package com.dtks.quickrecipe.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [RecipeDBEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

}