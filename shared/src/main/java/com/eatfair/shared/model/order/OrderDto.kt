package com.eatfair.shared.model.order

import com.eatfair.shared.constants.OrderStatus
import com.eatfair.shared.model.address.AddressDto
import com.eatfair.shared.model.restaurant.Restaurant

data class OrderTracking(
    val orderId: String = "",
    val restaurant: Restaurant? = null,
    val status: OrderStatus = OrderStatus.ORDER_PLACED,
    val deliveryPartner: DeliveryPartner = DeliveryPartner(),
    val estimatedTime: String = "",
    val pickupLocation: PickUpLocation = PickUpLocation(),
    val deliveryLocation: AddressDto? = null,
    val deliveryInstructions: String = "",
    val isDelayed: Boolean = false,
)

data class DeliveryPartner(
    val name: String = "",
    val phone: String = "",
    val avatar: String? = null
)

data class PickUpLocation(
    val address: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
)