package com.eatfair.partner.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eatfair.shared.constants.OrderStatus
import com.eatfair.shared.data.repo.OrderRepo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class OrdersUiState(
    val orders: List<OrderItem> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val orderRepo: OrderRepo,
    private val firestore: FirebaseFirestore
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()
    
    init {
        loadOrders()
    }
    
    private fun loadOrders() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            orderRepo.getOrdersForPartner(12).collect { orders ->
                val orderItems = orders.map { order ->
                    OrderItem(
                        id = order.orderId.replace("EF", "").toLongOrNull() ?: 0L,
                        customerName = "Customer", // Placeholder
                        customerPhone = "+1 555-0123", // Placeholder
                        items = listOf(
                            OrderMenuItem("Item 1", 1, 15.00), // Placeholder
                            OrderMenuItem("Item 2", 2, 10.00)  // Placeholder
                        ),
                        totalAmount = 35.00, // Placeholder
                        status = order.status,
                        time = order.estimatedTime,
                        deliveryAddress = order.deliveryLocation?.addressLine1() ?: "Pickup"
                    )
                }
                _uiState.update { it.copy(orders = orderItems, isLoading = false) }
            }
        }
    }
    
    fun acceptOrder(orderId: Long) {
        viewModelScope.launch {
            try {
                val orderIdStr = "EF$orderId"
                firestore.collection("orders")
                    .document(orderIdStr)
                    .update(
                        "status", "PREPARING",
                        "updatedAt", FieldValue.serverTimestamp()
                    )
                    .await()
                // Local state will update via real-time listener
            } catch (e: Exception) {
                println("Error accepting order: ${e.message}")
            }
        }
    }
    
    fun rejectOrder(orderId: Long) {
        viewModelScope.launch {
            try {
                val orderIdStr = "EF$orderId"
                firestore.collection("orders")
                    .document(orderIdStr)
                    .update(
                        "status", "REJECTED",
                        "updatedAt", FieldValue.serverTimestamp()
                    )
                    .await()
                // Remove from local state
                _uiState.update { state ->
                    state.copy(
                        orders = state.orders.filter { it.id != orderId }
                    )
                }
            } catch (e: Exception) {
                println("Error rejecting order: ${e.message}")
            }
        }
    }
    
    fun markAsPreparing(orderId: Long) {
        viewModelScope.launch {
            try {
                val orderIdStr = "EF$orderId"
                firestore.collection("orders")
                    .document(orderIdStr)
                    .update(
                        "status", "PREPARING",
                        "updatedAt", FieldValue.serverTimestamp()
                    )
                    .await()
            } catch (e: Exception) {
                println("Error updating order status: ${e.message}")
            }
        }
    }
    
    fun markAsReady(orderId: Long) {
        viewModelScope.launch {
            try {
                val orderIdStr = "EF$orderId"
                firestore.collection("orders")
                    .document(orderIdStr)
                    .update(
                        "status", "OUT_FOR_DELIVERY",
                        "updatedAt", FieldValue.serverTimestamp()
                    )
                    .await()
            } catch (e: Exception) {
                println("Error marking order as ready: ${e.message}")
            }
        }
    }
    
    private fun updateLocalOrderStatus(orderId: Long, newStatus: OrderStatus) {
        _uiState.update { state ->
            state.copy(
                orders = state.orders.map { order ->
                    if (order.id == orderId) {
                        order.copy(status = newStatus)
                    } else {
                        order
                    }
                }
            )
        }
    }
}
