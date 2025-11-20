package com.eatfair.app.ui.cart

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eatfair.shared.data.repo.AddressRepo
import com.eatfair.app.data.repo.PaymentRepo
import com.eatfair.shared.data.local.SessionManager
import com.eatfair.shared.model.address.AddressDto
import com.eatfair.shared.model.order.OrderTracking
import com.eatfair.shared.model.payment.PaymentSheetKeys
import com.eatfair.shared.model.restaurant.CartItem
import com.eatfair.shared.model.restaurant.MenuItem
import com.eatfair.shared.model.restaurant.Restaurant
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val addressRepo: AddressRepo,
    private val paymentRepo: PaymentRepo,
    private val sessionManager: SessionManager,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _cartRestaurant = MutableStateFlow<Restaurant?>(null)
    val cartRestaurant: StateFlow<Restaurant?> = _cartRestaurant.asStateFlow()

    private val _deliveryAddress = MutableStateFlow<AddressDto?>(null)
    val deliveryAddress = _deliveryAddress.asStateFlow()

    private val _paymentSheetEvent = MutableSharedFlow<PaymentSheetKeys>()
    val paymentSheetEvent = _paymentSheetEvent.asSharedFlow()

    private val _paymentErrorEvent = MutableSharedFlow<String>()
    val paymentErrorEvent = _paymentErrorEvent.asSharedFlow()

    private val _activeOrder = MutableStateFlow<OrderTracking?>(null)
    val activeOrder: StateFlow<OrderTracking?> = _activeOrder.asStateFlow()

//    private val _showOrderSuccessSheet = MutableStateFlow(false)
//    val showOrderSuccessSheet: StateFlow<Boolean> = _showOrderSuccessSheet.asStateFlow()

    init {
        fetchDefaultAddress()
    }

    fun updateRestaurant(restaurant: Restaurant) {
        _cartRestaurant.value = restaurant
    }

    fun updateItemQuantity(menuItem: MenuItem, newQuantity: Int) {
        val currentList = _cartItems.value.toMutableList()
        val existingItem = currentList.find { it.menuItem.id == menuItem.id }

        if (newQuantity > 0) {
            if (existingItem != null) {
                val itemIdx = currentList.indexOf(existingItem)
                currentList[itemIdx] = existingItem.copy(quantity = newQuantity)
            } else {
                currentList.add(CartItem(menuItem, newQuantity, ""))
            }
        } else {
            if (existingItem != null) {
                currentList.remove(existingItem)
            }
        }

        _cartItems.value = currentList

        // If the cart is now empty, clear the restaurant as well.
        if (currentList.isEmpty()) {
            _cartRestaurant.value = null
        }
    }

    fun updateItemQuantity(cartItem: CartItem, newQuantity: Int) {
        val currentList = _cartItems.value.toMutableList()
        val existingItem = cartItem

        if (newQuantity > 0) {
            val itemIdx = currentList.indexOf(existingItem)
            currentList[itemIdx] = existingItem.copy(quantity = newQuantity)
        } else {
            currentList.remove(existingItem)
        }
        _cartItems.value = currentList
    }


    fun removeFromCart(item: CartItem) {
        // Logic to remove an item from _cartItems.value
        _cartItems.value = _cartItems.value - item
    }

    fun clearCart() {
        // Logic to clear the cart
        _cartItems.value = emptyList()
        _cartRestaurant.value = null
    }

    fun fetchDefaultAddress() {
        viewModelScope.launch {
            val defaultAddress = addressRepo.getDefaultAddress()
            _deliveryAddress.value = defaultAddress
        }
    }

    fun updateDeliveryAddress(addressDto: AddressDto) {
        _deliveryAddress.value = addressDto
    }

    // This function will be called from the UI
    fun onPlaceOrderClicked(totalAmount: Double) {
        viewModelScope.launch {
            // Use the repository to get the keys
            val result = paymentRepo.getPaymentSheetKeys(totalAmount)

            result.onSuccess { keys ->
                // Emit the keys as a one-time event to the UI
                _paymentSheetEvent.emit(keys)
            }.onFailure { error ->
                // Emit an error event
                _paymentErrorEvent.emit(error.message ?: "An unknown error occurred")
            }
        }
    }

    fun onOrderPlacedSuccessfully(order: OrderTracking) {
        viewModelScope.launch {
            try {
                val userId = sessionManager.getUserId() ?: "anonymous"
                val restaurant = _cartRestaurant.value
                val items = _cartItems.value
                
                // Calculate total amount
                val subtotal = items.sumOf { (it.menuItem.price * it.quantity).toDouble() }
                val deliveryFee = 5.0
                val tax = subtotal * 0.08
                val totalAmount = subtotal + deliveryFee + tax
                
                // Create order data for Firestore
                val orderData = hashMapOf(
                    "orderId" to order.orderId,
                    "customerId" to userId,
                    "customerName" to (sessionManager.getUserName() ?: "Customer"),
                    "customerEmail" to (sessionManager.getUserEmail() ?: ""),
                    "restaurantId" to (restaurant?.id ?: 0),
                    "restaurantName" to (restaurant?.name ?: ""),
                    "restaurantAddress" to (restaurant?.address ?: ""),
                    "restaurantLatitude" to (restaurant?.latitude ?: 0.0),
                    "restaurantLongitude" to (restaurant?.longitude ?: 0.0),
                    "items" to items.map { cartItem ->
                        hashMapOf(
                            "id" to cartItem.menuItem.id,
                            "name" to cartItem.menuItem.name,
                            "price" to cartItem.menuItem.price,
                            "quantity" to cartItem.quantity
                        )
                    },
                    "subtotal" to subtotal,
                    "deliveryFee" to deliveryFee,
                    "tax" to tax,
                    "totalAmount" to totalAmount,
                    "status" to "ORDER_PLACED",
                    "deliveryAddress" to hashMapOf(
                        "completeAddress" to (order.deliveryLocation?.completeAddress ?: ""),
                        "houseNumber" to (order.deliveryLocation?.houseNumber ?: ""),
                        "apartmentRoad" to (order.deliveryLocation?.apartmentRoad ?: ""),
                        "latitude" to (order.deliveryLocation?.latitude ?: 0.0),
                        "longitude" to (order.deliveryLocation?.longitude ?: 0.0),
                        "phoneNumber" to (order.deliveryLocation?.phoneNumber ?: "")
                    ),
                    "deliveryInstructions" to (order.deliveryInstructions ?: ""),
                    "estimatedDeliveryTime" to order.estimatedTime,
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp(),
                    "driverId" to null,
                    "driverName" to null,
                    "isDelayed" to false
                )
                
                // Save order to Firestore
                firestore.collection("orders")
                    .document(order.orderId)
                    .set(orderData)
                    .await()
                
                // Update local state
                _activeOrder.value = order
                
                // Clear cart after successful order
                clearCart()
                
            } catch (e: Exception) {
                // Handle error - you might want to show an error message to the user
                println("Error saving order: ${e.message}")
            }
        }
    }

    fun dismissOrderSuccessSheet() {
//        _showOrderSuccessSheet.value = false
    }

    fun completeActiveOrder() {
        _activeOrder.value = null
    }
}