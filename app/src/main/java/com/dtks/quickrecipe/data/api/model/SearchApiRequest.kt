package com.dtks.quickrecipe.data.api.model

import com.dtks.quickrecipe.domain.SearchType

data class SearchApiRequest(
    val searchPhrase: String,
    val searchType: SearchType,
    val offset: Int = 0
)