package com.dtks.quickrecipe.domain

data class SearchResult(
    val recipes: List<RecipeDomainEntity>,
    val messageToShow: String? = null,
    val canLoadMore: Boolean
)