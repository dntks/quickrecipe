package com.dtks.quickrecipe.di

import android.content.Context
import androidx.room.Room
import com.dtks.quickrecipe.data.local.FavoriteRecipeDao
import com.dtks.quickrecipe.data.local.RecipeDao
import com.dtks.quickrecipe.data.local.RecipeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): RecipeDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            RecipeDatabase::class.java,
            "QuickRecipe.db"
        ).build()
    }

    @Provides
    fun provideRecipeDao(database: RecipeDatabase): RecipeDao = database.recipeDao()

    @Provides
    fun provideFavRecipeDao(database: RecipeDatabase): FavoriteRecipeDao =
        database.favoriteRecipeDao()

}
