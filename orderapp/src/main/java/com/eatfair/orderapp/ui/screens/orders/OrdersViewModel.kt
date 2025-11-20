package com.eatfair.orderapp.ui.screens.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eatfair.orderapp.data.repo.OrdersRepo
import com.eatfair.orderapp.model.order.EarningsSummary
import com.eatfair.orderapp.model.order.OrderHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

sealed class OrdersUiState {
    object Loading : OrdersUiState()
    data class Success(
        val orders: List<OrderHistory>,
        val earnings: EarningsSummary
    ) : OrdersUiState()

    object Empty : OrdersUiState()
    data class Error(val message: String) : OrdersUiState()
}

sealed class OrderDetailUiState {
    object Loading : OrderDetailUiState()
    data class Success(val order: OrderHistory) : OrderDetailUiState()
    data class Error(val message: String) : OrderDetailUiState()
}

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val ordersRepo: OrdersRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow<OrdersUiState>(OrdersUiState.Loading)
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()

    private val _detailsUiState = MutableStateFlow<OrderDetailUiState>(OrderDetailUiState.Loading)
    val detailsUiState: StateFlow<OrderDetailUiState> = _detailsUiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            _uiState.value = OrdersUiState.Loading
            try {
                combine(
                    ordersRepo.getOrderHistory(),
                    ordersRepo.getEarningsSummary()
                ) { orders, earnings ->
                    if (orders.isEmpty()) {
                        OrdersUiState.Empty
                    } else {
                        OrdersUiState.Success(orders, earnings)
                    }
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: Exception) {
                _uiState.value = OrdersUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun searchOrders(query: String) {
        _searchQuery.value = query
        if (query.isEmpty()) {
            loadOrders()
            return
        }

        viewModelScope.launch {
            try {
                combine(
                    ordersRepo.searchOrders(query),
                    ordersRepo.getEarningsSummary()
                ) { orders, earnings ->
                    if (orders.isEmpty()) {
                        OrdersUiState.Empty
                    } else {
                        OrdersUiState.Success(orders, earnings)
                    }
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: Exception) {
                _uiState.value = OrdersUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadOrderDetail(orderId: String) {
        viewModelScope.launch {
            _detailsUiState.value = OrderDetailUiState.Loading
            try {
                val order = ordersRepo.getOrderDetail(orderId)
                _detailsUiState.value = if (order != null) {
                    OrderDetailUiState.Success(order)
                } else {
                    OrderDetailUiState.Error("Order not found")
                }
            } catch (e: Exception) {
                _detailsUiState.value = OrderDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}