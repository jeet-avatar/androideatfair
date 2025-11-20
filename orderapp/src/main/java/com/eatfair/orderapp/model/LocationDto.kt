package com.eatfair.orderapp.model

data class LocationDto(
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val landmark: String? = null
)