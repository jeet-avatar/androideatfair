package com.eatfair.shared.data.repo

import com.eatfair.shared.constants.OrderStatus
import com.eatfair.shared.model.order.DeliveryPartner
import com.eatfair.shared.model.order.OrderTracking
import com.eatfair.shared.model.order.PickUpLocation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepo @Inject constructor(
    private val restaurantRepo: RestaurantRepo,
    private val firestore: com.google.firebase.firestore.FirebaseFirestore
) {

    private val dummyOrders = listOf(
        OrderTracking(
            orderId = "EF1001",
            restaurant = restaurantRepo.getRestaurantById(12),
            status = OrderStatus.ORDER_PLACED,
            deliveryPartner = DeliveryPartner("Jithesh Manoharan", "+1-949-123-4567"),
            estimatedTime = "25 mins",
            pickupLocation = PickUpLocation("22205 El Paseo", 33.6402, -117.6033),
            deliveryLocation = null,
            deliveryInstructions = "Leave at door",
            isDelayed = false
        ),
        OrderTracking(
            orderId = "EF1002",
            restaurant = restaurantRepo.getRestaurantById(12),
            status = OrderStatus.PREPARING,
            deliveryPartner = DeliveryPartner("Sarah Jones", "+1-555-0102"),
            estimatedTime = "15 mins",
            pickupLocation = PickUpLocation("22205 El Paseo", 33.6402, -117.6033),
            deliveryLocation = null,
            deliveryInstructions = "",
            isDelayed = true
        ),
        OrderTracking(
            orderId = "EF1003",
            restaurant = restaurantRepo.getRestaurantById(2),
            status = OrderStatus.OUT_FOR_DELIVERY,
            deliveryPartner = DeliveryPartner("Mike Smith", "+1-555-0103"),
            estimatedTime = "5 mins",
            pickupLocation = PickUpLocation("Chandni Chowk", 28.6508, 77.2300),
            deliveryLocation = null,
            deliveryInstructions = "Call upon arrival",
            isDelayed = false
        )
    )

    fun getOrdersForPartner(partnerId: Int): Flow<List<OrderTracking>> = kotlinx.coroutines.flow.callbackFlow {
        val collection = firestore.collection("orders")
        // Filter by partnerId if needed, e.g., .whereEqualTo("restaurant.id", partnerId)
        
        val listener = collection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            
            if (snapshot != null && !snapshot.isEmpty) {
                val orders = snapshot.toObjects(OrderTracking::class.java)
                trySend(orders)
            } else {
                trySend(dummyOrders) // Fallback
            }
        }
        
        awaitClose { listener.remove() }
    }

    fun getActiveOrder(): Flow<OrderTracking?> = kotlinx.coroutines.flow.callbackFlow {
         val collection = firestore.collection("orders")
         // In real app, filter by userId
         
         val listener = collection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            
            if (snapshot != null && !snapshot.isEmpty) {
                val orders = snapshot.toObjects(OrderTracking::class.java)
                trySend(orders.firstOrNull())
            } else {
                trySend(dummyOrders.firstOrNull()) // Fallback
            }
        }
        
        awaitClose { listener.remove() }
    }
}
