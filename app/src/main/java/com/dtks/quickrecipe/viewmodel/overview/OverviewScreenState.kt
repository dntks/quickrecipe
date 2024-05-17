package com.dtks.quickrecipe.viewmodel.overview


sealed class OverviewScreenState<out T>(val stateData: T?)

data class OverviewScreenLoading<out T>(val data: T? = null) : OverviewScreenState<T>(data)
data class RecipesLoaded<out T>(val data: T) : OverviewScreenState<T>(data)
data class OverviewError<out T>(val data: T? = null) : OverviewScreenState<T>(data)