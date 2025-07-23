package com.dtks.quickrecipe.data.api.model

import com.dtks.quickrecipe.data.repository.model.SearchType

data class SearchApiRequest(
    val searchPhrase: String,
    val searchType: SearchType,
    val offset: Int = 0
)