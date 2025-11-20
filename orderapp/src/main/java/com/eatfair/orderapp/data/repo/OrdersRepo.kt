package com.eatfair.orderapp.data.repo

import com.eatfair.orderapp.model.order.EarningsSummary
import com.eatfair.orderapp.model.order.OrderHistory
import kotlinx.coroutines.flow.Flow

interface OrdersRepo {

    fun getOrderHistory(): Flow<List<OrderHistory>>

    fun getEarningsSummary(): Flow<EarningsSummary>

    suspend fun getOrderDetail(orderId: String): OrderHistory?

    fun searchOrders(query: String): Flow<List<OrderHistory>>
}