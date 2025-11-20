package com.eatfair.orderapp.data.repo.impl

import com.eatfair.orderapp.constants.OrderStatus
import com.eatfair.orderapp.data.repo.DeliveryRepo
import com.eatfair.orderapp.model.LocationDto
import com.eatfair.orderapp.model.order.DeliveryOrder
import com.eatfair.orderapp.model.order.OrderItem
import com.eatfair.orderapp.ui.screens.home.DeliveryStats
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeliveryRepoImpl @Inject constructor() : DeliveryRepo {

    private var currentOrder: DeliveryOrder? = createMockOrder()

    override suspend fun getActiveOrder(): DeliveryOrder? {
        delay(1000) // Simulate network call
        return currentOrder
    }

    override suspend fun updateOrderStatus(orderId: String, status: OrderStatus): Result<Unit> {
        delay(500)
        currentOrder = currentOrder?.copy(status = status)
        return Result.success(Unit)
    }

    override suspend fun getCurrentOrderStatus(orderId: String): OrderStatus {
//        delay(500)
        val status = currentOrder?.status ?: OrderStatus.PENDING
        return status
    }

    override fun getDeliveryStats(): Flow<DeliveryStats> = flow {
        delay(800)
        emit(
            DeliveryStats(
                todayOrders = 8,
                todayEarnings = 450.0,
                weeklyOrders = 45,
                weeklyEarnings = 2850.0,
                rating = 4.8,
                completionRate = 98.5
            )
        )
    }

    private fun createMockOrder() = DeliveryOrder(
        orderId = "ORD${System.currentTimeMillis()}",
        invoiceId = "INV-2024-${(1000..9999).random()}",
        customerName = "Rajesh Kumar",
        customerPhone = "+91 98765 43210",
        customerAvatar = "https://cdn.pixabay.com/photo/2012/04/18/23/36/boy-38262_1280.png",
        items = listOf(
            OrderItem("Chicken Biryani", 2, 350.0, "O1"),
            OrderItem("Paneer Butter Masala", 1, 280.0,"O2"),
            OrderItem("Naan", 4, 60.0, "O3"),
            OrderItem("Raita", 1, 40.0,"O4")
        ),
        totalAmount = 730.0,
        pickupLocation = LocationDto(
            address = "Spice Garden Restaurant, 82 W 14th St, New York",
            latitude = 40.738,
            longitude = -73.999,
            landmark = "Near City Mall"
        ),
        deliveryLocation = LocationDto(
            address = "155 W 51st St, New York",
            latitude = 40.760,
            longitude = -73.983,
            landmark = "Opposite to Park View"
        ),
        pickupTime = "6:30 PM",
        estimatedDeliveryTime = "7:15 PM",
        status = OrderStatus.PENDING,
        distance = "3.2 km",
        earnings = 85.0
    )
}