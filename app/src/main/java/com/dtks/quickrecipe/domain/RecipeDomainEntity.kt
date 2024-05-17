package com.dtks.quickrecipe.domain

data class RecipeDomainEntity(
    val id: Long,
    val title: String,
    val imageUrl: String,
    val ingredients: String
)