package com.eatfair.orderapp.model.order

import com.eatfair.orderapp.constants.OrderHistoryStatus

data class OrderHistory(
    val orderId: String,
    val invoiceId: String,
    val restaurantName: String,
    val amount: Double,
    val date: String,
    val status: OrderHistoryStatus,
    val customerName: String,
    val customerAddress: String,
    val restaurantAddress: String,
    val items: List<OrderHistoryItem>
)

data class OrderHistoryItem(
    val name: String,
    val description: String,
    val quantity: Int,
    val price: Double,
    val imageUrl: String? = null
)

data class EarningsSummary(
    val balance: Double,
    val totalEarned: Double
)