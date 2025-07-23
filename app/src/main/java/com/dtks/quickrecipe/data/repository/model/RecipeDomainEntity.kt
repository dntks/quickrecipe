package com.dtks.quickrecipe.data.repository.model

data class RecipeDomainEntity(
    val id: Long,
    val title: String,
    val imageUrl: String,
    val ingredients: String
)