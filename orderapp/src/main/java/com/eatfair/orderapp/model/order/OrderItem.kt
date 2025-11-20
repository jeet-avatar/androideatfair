package com.eatfair.orderapp.model.order

data class OrderItem(
    val name: String,
    val quantity: Int,
    val price: Double,
    val id: String,
    val description: String = ""
)
