package com.eatfair.orderapp.data.repo

import com.eatfair.orderapp.constants.DeliveryStatus
import com.eatfair.orderapp.constants.OrderStatus
import com.eatfair.orderapp.model.order.DeliveryOrder
import com.eatfair.orderapp.ui.screens.home.DeliveryStats
import kotlinx.coroutines.flow.Flow

interface DeliveryRepo {
    suspend fun getActiveOrder(): DeliveryOrder?
    suspend fun updateOrderStatus(orderId: String, status: OrderStatus): Result<Unit>

    suspend fun getCurrentOrderStatus(orderId: String): OrderStatus
    fun getDeliveryStats(): Flow<DeliveryStats>
}