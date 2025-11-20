package com.eatfair.orderapp.model.order

import com.eatfair.orderapp.constants.OrderStatus
import com.eatfair.orderapp.model.LocationDto

data class DeliveryOrder(
    val orderId: String,
    val invoiceId: String,
    val customerName: String,
    val customerPhone: String,
    val customerAvatar: String? = null,
    val items: List<OrderItem>,
    val totalAmount: Double,
    val pickupLocation: LocationDto,
    val deliveryLocation: LocationDto,
    val pickupTime: String,
    val estimatedDeliveryTime: String,
    val status: OrderStatus,
    val distance: String,
    val earnings: Double
)