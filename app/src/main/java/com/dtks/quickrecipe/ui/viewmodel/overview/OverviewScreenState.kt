package com.dtks.quickrecipe.ui.viewmodel.overview


sealed class OverviewScreenState<out T>(val stateData: T?)

data class OverviewScreenLoading<out T>(val data: T? = null) : OverviewScreenState<T>(data)
data class RecipesLoaded<out T>(val data: T, val message: String? = null) : OverviewScreenState<T>(data)
data class OverviewError<out T>(val data: T? = null) : OverviewScreenState<T>(data)