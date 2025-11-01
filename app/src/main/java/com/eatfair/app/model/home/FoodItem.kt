package com.eatfair.app.model.home

data class FoodItem(
    val id: Int,
    val name: String,
    val price: Double,
    val rating: Double,
    val imageUrl: String? = null
)
