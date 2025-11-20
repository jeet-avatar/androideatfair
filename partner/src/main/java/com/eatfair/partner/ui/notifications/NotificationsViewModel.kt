package com.eatfair.partner.ui.notifications

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class NotificationsUiState(
    val notifications: List<PartnerNotification> = emptyList()
)

@HiltViewModel
class NotificationsViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()
    
    init {
        loadNotifications()
    }
    
    private fun loadNotifications() {
        // TODO: Replace with actual repository call
        _uiState.value = NotificationsUiState(
            notifications = listOf(
                PartnerNotification(
                    id = 1,
                    title = "New Order Received!",
                    message = "Order #1005 from Charlie Brown - $92.00",
                    time = "2 mins ago",
                    type = NotificationType.NEW_ORDER,
                    isRead = false
                ),
                PartnerNotification(
                    id = 2,
                    title = "New Order Received!",
                    message = "Order #1001 from John Doe - $63.00",
                    time = "5 mins ago",
                    type = NotificationType.NEW_ORDER,
                    isRead = false
                ),
                PartnerNotification(
                    id = 3,
                    title = "Payment Received",
                    message = "Payment of $44.00 received for Order #1003",
                    time = "30 mins ago",
                    type = NotificationType.PAYMENT_RECEIVED,
                    isRead = true
                ),
                PartnerNotification(
                    id = 4,
                    title = "New Review",
                    message = "You received a 5-star review from Alice Williams",
                    time = "1 hour ago",
                    type = NotificationType.REVIEW_RECEIVED,
                    isRead = true
                ),
                PartnerNotification(
                    id = 5,
                    title = "System Update",
                    message = "New features available in the partner app. Check them out!",
                    time = "2 hours ago",
                    type = NotificationType.SYSTEM,
                    isRead = true
                )
            )
        )
    }
    
    fun markAsRead(notificationId: Int) {
        _uiState.update { state ->
            state.copy(
                notifications = state.notifications.map { notification ->
                    if (notification.id == notificationId) {
                        notification.copy(isRead = true)
                    } else {
                        notification
                    }
                }
            )
        }
        // TODO: Call repository to mark as read
    }
    
    fun clearAll() {
        _uiState.update { state ->
            state.copy(notifications = emptyList())
        }
        // TODO: Call repository to clear all
    }
    
    // This would be called by a background service or FCM
    fun addNotification(notification: PartnerNotification) {
        _uiState.update { state ->
            state.copy(
                notifications = listOf(notification) + state.notifications
            )
        }
    }
}
