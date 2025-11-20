package com.eatfair.orderapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eatfair.orderapp.constants.DeliveryStatus
import com.eatfair.orderapp.constants.OrderStatus
import com.eatfair.orderapp.data.repo.DeliveryRepo
import com.eatfair.orderapp.model.order.DeliveryOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.jvm.optionals.getOrNull

sealed class DeliveryUiState {
    object Loading : DeliveryUiState()
    object NoOrders : DeliveryUiState()
    data class Active(val order: DeliveryOrder) : DeliveryUiState()
    data class Error(val message: String) : DeliveryUiState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val deliveryRepo: DeliveryRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow<DeliveryUiState>(DeliveryUiState.Loading)
    val uiState: StateFlow<DeliveryUiState> = _uiState.asStateFlow()

    private val _showConfirmDialog = MutableStateFlow(false)
    val showConfirmDialog: StateFlow<Boolean> = _showConfirmDialog.asStateFlow()

    private val _orderStatus = MutableStateFlow<OrderStatus?>(null)
    val orderStatus: StateFlow<OrderStatus?> = _orderStatus

    init {
        loadActiveOrder()
    }

    fun loadActiveOrder() {
        viewModelScope.launch {
            _uiState.value = DeliveryUiState.Loading
            try {
                val order = deliveryRepo.getActiveOrder()
                _uiState.value = if (order != null) {
                    DeliveryUiState.Active(order)
                } else {
                    DeliveryUiState.NoOrders
                }
            } catch (e: Exception) {
                _uiState.value = DeliveryUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        viewModelScope.launch {
            deliveryRepo.updateOrderStatus(orderId, newStatus)

            val order = deliveryRepo.getActiveOrder()

            if (order != null) {
                _uiState.value = DeliveryUiState.Active(order)
            }
//            loadActiveOrder()
        }
    }

    suspend fun getCurrentOrderStatus(orderId: String): OrderStatus {
        return deliveryRepo.getCurrentOrderStatus(orderId)
    }

    fun startNavigation() {
        val currentState = _uiState.value
        if (currentState is DeliveryUiState.Active) {
            viewModelScope.launch {
                val orderStatus = getCurrentOrderStatus(currentState.order.orderId)
                updateOrderStatus(currentState.order.orderId, OrderStatus.EN_ROUTE_TO_PICKUP)
            }
        }
    }

    fun confirmPickup() {
        val currentState = _uiState.value
        if (currentState is DeliveryUiState.Active) {
            viewModelScope.launch {
                val orderStatus = getCurrentOrderStatus(currentState.order.orderId)
                if (orderStatus == OrderStatus.EN_ROUTE_TO_PICKUP) {
                    updateOrderStatus(currentState.order.orderId, OrderStatus.EN_ROUTE_TO_DELIVERY)
                } else {
                    updateOrderStatus(currentState.order.orderId, OrderStatus.ARRIVED_AT_DELIVERY)
                }
            }
        }
    }

    fun confirmDelivery() {
        val currentState = _uiState.value
        if (currentState is DeliveryUiState.Active) {
            updateOrderStatus(currentState.order.orderId, OrderStatus.DELIVERED)
            _showConfirmDialog.value = false

        }
    }

    fun showConfirmDeliveryDialog() {
        _showConfirmDialog.value = true
    }

    fun hideConfirmDeliveryDialog() {
        _showConfirmDialog.value = false
    }
}