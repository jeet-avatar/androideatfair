package com.eatfair.shared.model.search

data class SearchResultDto(
    val id: Int,
    val name: String,
    val category: String,
    val rating: Double,
    val imageUrl: String? = null
)
