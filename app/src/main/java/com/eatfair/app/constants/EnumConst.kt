package com.eatfair.app.constants

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector

enum class AddressType(
    val displayName: String,
    val icon: ImageVector
) {
    HOME("Home", Icons.Default.Home),
    WORK("Work", Icons.Default.Work),
    FRIENDS("Friends", Icons.Default.Apartment),
    FAMILY("Family", Icons.Default.FamilyRestroom),
    OTHER("Hotel", Icons.Default.LocationOn),
}


enum class OrderStatus {
    ORDER_PLACED,
    PREPARING,
    OUT_FOR_DELIVERY,
    DELIVERED
}