package com.eatfair.orderapp.data.repo.impl

import com.eatfair.orderapp.constants.OrderHistoryStatus
import com.eatfair.orderapp.data.repo.OrdersRepo
import com.eatfair.orderapp.model.order.EarningsSummary
import com.eatfair.orderapp.model.order.OrderHistory
import com.eatfair.orderapp.model.order.OrderHistoryItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OrdersRepoImpl @Inject constructor(

) : OrdersRepo {

    private val mockOrders = listOf(
        OrderHistory(
            orderId = "30VN15VD57",
            invoiceId = "30VN15VD57",
            restaurantName = "Monicas Trattoria",
            amount = 33.75,
            date = "20 Oct, 2024",
            status = OrderHistoryStatus.IN_PROGRESS,
            customerName = "Sabrina Lorenshtein",
            customerAddress = "155 W 51st St, New York",
            restaurantAddress = "82 W 14th St, New York",
            items = listOf(
                OrderHistoryItem(
                    name = "Chocomocco",
                    description = "A cupcake is a small, single-serving cake typically...",
                    quantity = 1,
                    price = 8.00
                ),
                OrderHistoryItem(
                    name = "FoodDoor Fries",
                    description = "Fries are crispy, golden strips of fried potato.",
                    quantity = 1,
                    price = 8.25
                ),
                OrderHistoryItem(
                    name = "Burger King",
                    description = "Served in a bun with toppings like lettuce, tomato, & cheese.",
                    quantity = 1,
                    price = 14.50
                ),
                OrderHistoryItem(
                    name = "Coca-cola (330ml)",
                    description = "Coca-Cola is a refreshing, carbonated soft drink.",
                    quantity = 1,
                    price = 3.00
                )
            )
        ),
        OrderHistory(
            orderId = "5NSF72X2684PQ",
            invoiceId = "5NSF72X2684PQ",
            restaurantName = "Fast Food Burgeriata",
            amount = 12.49,
            date = "28 Oct, 2024",
            status = OrderHistoryStatus.COMPLETED,
            customerName = "John Doe",
            customerAddress = "123 Main St, New York",
            restaurantAddress = "456 Food Ave, New York",
            items = listOf()
        ),
        OrderHistory(
            orderId = "6BJK470P57LM",
            invoiceId = "6BJK470P57LM",
            restaurantName = "Piccola Cucina Osteria",
            amount = 12.49,
            date = "02 Nov, 2024",
            status = OrderHistoryStatus.COMPLETED,
            customerName = "Jane Smith",
            customerAddress = "789 Oak St, New York",
            restaurantAddress = "321 Italian Way, New York",
            items = listOf()
        ),
        OrderHistory(
            orderId = "7MQF3ZKLR24UT",
            invoiceId = "7MQF3ZKLR24UT",
            restaurantName = "Manhattan Morsels",
            amount = 12.49,
            date = "12 Nov, 2024",
            status = OrderHistoryStatus.COMPLETED,
            customerName = "Mike Johnson",
            customerAddress = "456 Park Ave, New York",
            restaurantAddress = "789 Broadway, New York",
            items = listOf()
        ),
        OrderHistory(
            orderId = "4RzdR75S7747W",
            invoiceId = "4RzdR75S7747W",
            restaurantName = "The Bronx Bistro",
            amount = 12.49,
            date = "16 Nov, 2024",
            status = OrderHistoryStatus.CANCELLED,
            customerName = "Sarah Williams",
            customerAddress = "321 Bronx Blvd, New York",
            restaurantAddress = "654 Bistro Lane, New York",
            items = listOf()
        )
    )

    override fun getOrderHistory(): Flow<List<OrderHistory>> = flow {
        delay(800)
        emit(mockOrders)
    }

    override fun getEarningsSummary(): Flow<EarningsSummary> = flow {
        delay(600)
        emit(EarningsSummary(balance = 0.0, totalEarned = 0.0))
    }

    override suspend fun getOrderDetail(orderId: String): OrderHistory? {
        delay(500)
        return mockOrders.find { it.orderId == orderId }
    }

    override fun searchOrders(query: String): Flow<List<OrderHistory>> = flow {
        delay(300)
        val filtered = mockOrders.filter {
            it.restaurantName.contains(query, ignoreCase = true) ||
                    it.orderId.contains(query, ignoreCase = true) ||
                    it.customerName.contains(query, ignoreCase = true)
        }
        emit(filtered)
    }
}