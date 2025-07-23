package com.dtks.quickrecipe.data.repository.model

import com.dtks.quickrecipe.data.repository.model.RecipeDomainEntity

data class SearchResult(
    val recipes: List<RecipeDomainEntity>,
    val messageToShow: String? = null,
    val canLoadMore: Boolean
)