package com.eatfair.app.model.order

import com.eatfair.app.constants.OrderStatus
import com.eatfair.app.model.address.AddressDto
import com.eatfair.app.model.restaurant.Restaurant

data class OrderTracking(
    val orderId: String,
    val restaurant: Restaurant?,
    val status: OrderStatus,
    val deliveryPartner: DeliveryPartner,
    val estimatedTime: String,
    val pickupLocation: PickUpLocation,
    val deliveryLocation: AddressDto?,
    val deliveryInstructions: String = "",
    val isDelayed: Boolean = false,
)

data class DeliveryPartner(
    val name: String,
    val phone: String,
    val avatar: String? = null
)

data class PickUpLocation(
    val address: String,
    val lat: Double,
    val lng: Double,
)