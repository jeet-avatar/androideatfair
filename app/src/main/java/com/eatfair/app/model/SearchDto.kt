package com.eatfair.app.model

data class SearchResultDto(
    val id: Int,
    val name: String,
    val category: String,
    val rating: Double,
    val imageUrl: String? = null
)
