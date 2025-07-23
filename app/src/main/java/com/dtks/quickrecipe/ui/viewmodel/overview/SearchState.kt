package com.dtks.quickrecipe.ui.viewmodel.overview

import com.dtks.quickrecipe.data.repository.model.SearchType

//This class is responsible for storing the different UI setups
data class SearchState(
    val searchType: SearchType = SearchType.BY_TITLE,
    val searchText: String = "",
    val offset: Int = 0,
    val canLoadMore: Boolean = true
)