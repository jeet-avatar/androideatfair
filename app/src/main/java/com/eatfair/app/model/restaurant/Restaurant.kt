package com.eatfair.app.model.restaurant

data class Restaurant(
    val id: Int,
    val name: String,
    val rating: Float,
    val reviews: String,
    val distance: String,
    val deliveryTime: String,
    val cuisineType: String,
    val isPureVeg: Boolean,
    val isOpen: Boolean,
    val tags: List<String>,
    val imageUrl: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)