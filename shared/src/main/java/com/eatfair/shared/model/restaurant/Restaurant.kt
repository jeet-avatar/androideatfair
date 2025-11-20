package com.eatfair.shared.model.restaurant

data class Restaurant(
    val id: Int = 0,
    val name: String = "",
    val rating: Float = 0f,
    val reviews: String = "",
    val distance: String = "",
    val deliveryTime: String = "",
    val cuisineType: String = "",
    val isPureVeg: Boolean = false,
    val isOpen: Boolean = false,
    val tags: List<String> = emptyList(),
    val imageUrl: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)