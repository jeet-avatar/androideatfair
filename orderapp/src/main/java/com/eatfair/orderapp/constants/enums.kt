package com.eatfair.orderapp.constants

import com.eatfair.orderapp.R

enum class BottomNavItems(val label: String, val iconResId: Int) {
    HOME("Home", R.drawable.ic_home),
    Orders("Orders", R.drawable.ic_list),
    EARNINGS("Earnings", R.drawable.ic_dollar),
    PROFILE("Profile", R.drawable.ic_profile_setting)
}

enum class VehicleType(val displayName: String) {
    CAR("Car"),
    MOTORCYCLE("Motorcycle"),
    BICYCLE("Bicycle"),
    SCOOTER("Scooter")
}

enum class FuelType {
    PETROL,
    DIESEL,
    ELECTRIC,
    HYBRID
}

enum class DeliveryStatus {
    BEFORE_PICKUP,
    EN_ROUTE,
    DELIVERED
}

enum class OrderStatus {
    PENDING,
    EN_ROUTE_TO_PICKUP,
    ARRIVED_AT_PICKUP,
    PICKED_UP,
    EN_ROUTE_TO_DELIVERY,
    ARRIVED_AT_DELIVERY,
    DELIVERED
}

enum class OrderHistoryStatus {
    COMPLETED,
    CANCELLED,
    IN_PROGRESS
}

enum class OrderTab(val displayName: String) {
    ACTIVE("Active"),
    SCHEDULED("Scheduled"),
    COMPLETED("Completed"),
    REJECTED("Rejected")
}

enum class ReportPeriod {
    TODAY, WEEKLY, MONTHLY
}

enum class PaymentMethod(val displayName: String) {
    ONLINE("Online Payment"),
    CASH("Cash on Delivery"),
    CARD("Card Payment")
}

enum class EarningsFilterType {
    SUCCESS_ORDERS,
    DECLINED_ORDERS,
    TOTAL_EARNED,
    TOTAL_WITHDRAW
}