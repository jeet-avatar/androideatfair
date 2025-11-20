package com.eatfair.orderapp.ui.screens.home.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.BikeScooter
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DriveEta
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.eatfair.orderapp.constants.OrderStatus
import com.eatfair.orderapp.model.LocationDto
import com.eatfair.orderapp.model.order.DeliveryOrder
import com.eatfair.orderapp.model.order.OrderItem

@Composable
fun OrderBottomSheet(
    order: DeliveryOrder,
    onStartNavigation: () -> Unit,
    onPickupConfirm: () -> Unit,
    onDeliveryConfirm: () -> Unit,
    onCallCustomer: () -> Unit,
    onChatCustomer: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()) // Makes the content scrollable
    ) {

        // Customer Info Header
        CustomerInfoHeader(
            order = order,
            onCallCustomer = onCallCustomer,
            onChatCustomer = onChatCustomer
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Order Status Indicator
        OrderStatusIndicator(order = order)

        // Warning Message
        if (order.status == OrderStatus.EN_ROUTE_TO_DELIVERY) {

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF3E0), RoundedCornerShape(8.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(48.dp)
                        .background(Color(0xFFFF9800))
                )
                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Wait for the customer",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = Color(0xFF333333)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Notified the customer. You can leave the order if you can't find them soon.",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons
        ActionButtons(
            status = order.status,
            onStartNavigation = onStartNavigation,
            onPickupConfirm = onPickupConfirm,
            onDeliveryConfirm = onDeliveryConfirm,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Invoice and Earnings
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Invoice ID",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = order.invoiceId,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Your Earnings",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = "₹${order.earnings}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Location Details
        LocationDetails(order = order)

        Spacer(modifier = Modifier.height(16.dp))

        // Order Items (Expandable)
        OrderItemsList(
            items = order.items,
            totalAmount = order.totalAmount,
            isExpanded = isExpanded,
            onToggle = { isExpanded = !isExpanded }
        )

        Spacer(modifier = Modifier.height(20.dp))

    }

}

@Preview(showBackground = true)
@Composable
fun CustomHeaderPreview() {
    CustomerInfoHeader(
        order = DeliveryOrder(
            orderId = "ORD${System.currentTimeMillis()}",
            invoiceId = "INV-2024-${(1000..9999).random()}",
            customerName = "Rajesh Kumar",
            customerPhone = "+91 98765 43210",
            customerAvatar = null,
            items = listOf(
                OrderItem("Chicken Biryani", 2, 350.0, "O1"),
                OrderItem("Paneer Butter Masala", 1, 280.0, "O2"),
                OrderItem("Naan", 4, 60.0, "O3"),
                OrderItem("Raita", 1, 40.0, "O4")
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
    )
}


@Composable
fun CustomerInfoHeader(
    order: DeliveryOrder,
    onCallCustomer: () -> Unit = {},
    onChatCustomer: () -> Unit = {}
) {
    Column() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar with Action Buttons

            Row {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    if (order.customerAvatar != null) {
                        AsyncImage(
                            model = order.customerAvatar,
                            contentDescription = "Customer Profile",
                        )
                    } else {
                        Text(
                            text = order.customerName.first().toString(),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }


                Column(
                    modifier = Modifier.padding(start = 12.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = order.customerName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = order.customerPhone,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }


            val (iconBg, icon) = when (order.status) {
                OrderStatus.PENDING -> Color(0xFFE8E8E8) to Icons.Default.Circle
                OrderStatus.PICKED_UP -> Color(0xFF733CD5) to Icons.Default.DriveEta
                OrderStatus.EN_ROUTE_TO_DELIVERY -> Color(0xFFC2B439) to Icons.Default.BikeScooter
                OrderStatus.DELIVERED -> Color(0xFF4CAF50) to Icons.Default.CheckCircle
                else -> Color(0xFF4CAF50) to Icons.Default.CheckCircle
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconBg, CircleShape)
                    .align(Alignment.CenterVertically),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (order.status == OrderStatus.PENDING) Color(0xFFBBBBBB) else Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Chat and Call Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onChatCustomer,
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Chat,
                    contentDescription = "Chat",
                    modifier = Modifier.size(20.dp),
                    tint = Color(0xFF666666)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Chat",
                    color = Color(0xFF333333),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Button(
                onClick = onCallCustomer,
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF5252)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Call",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Call",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun OrderStatusIndicator(
    order: DeliveryOrder,
) {
    val status = order.status
    val statusText = when (status) {
        OrderStatus.PENDING -> "Ready for Pickup"
        OrderStatus.EN_ROUTE_TO_PICKUP -> "On the way to Restaurant"
        OrderStatus.ARRIVED_AT_PICKUP -> "Arrived at Restaurant"
        OrderStatus.PICKED_UP -> "Order Picked Up"
        OrderStatus.EN_ROUTE_TO_DELIVERY -> "On the way to Customer"
        OrderStatus.ARRIVED_AT_DELIVERY -> "Arrived at Destination"
        OrderStatus.DELIVERED -> "Order Delivered"
    }

    val statusColor = when (status) {
        OrderStatus.PENDING -> MaterialTheme.colorScheme.error
        OrderStatus.DELIVERED -> Color(0xFF4CAF50)
        else -> MaterialTheme.colorScheme.primary
    }

    val statusText2 = when (status) {
        OrderStatus.PENDING -> "Pickup by: ${order.pickupTime}"
        OrderStatus.EN_ROUTE_TO_PICKUP -> ""
        OrderStatus.ARRIVED_AT_PICKUP -> ""
        OrderStatus.PICKED_UP -> ""
        OrderStatus.EN_ROUTE_TO_DELIVERY -> ""
        OrderStatus.ARRIVED_AT_DELIVERY -> ""
        OrderStatus.DELIVERED -> ""
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(statusColor.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocalShipping,
            contentDescription = null,
            tint = statusColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Column(verticalArrangement = Arrangement.SpaceEvenly) {

            Text(
                text = statusText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = statusColor
            )

            if (statusText2.isNotEmpty()) {
                Text(
                    text = statusText2,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = statusColor
                )
            }
        }
    }
}

@Composable
fun LocationDetails(order: DeliveryOrder) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        // Pickup Location
        LocationItem(
            icon = Icons.Default.Restaurant,
            title = "Pickup from",
            address = order.pickupLocation.address,
            time = order.pickupTime,
            iconColor = MaterialTheme.colorScheme.primary
        )

        // Distance Line
        Box(
            modifier = Modifier
                .padding(start = 20.dp, top = 8.dp, bottom = 8.dp)
                .width(2.dp)
                .height(24.dp)
                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
        )

        // Delivery Location
        LocationItem(
            icon = Icons.Default.LocationOn,
            title = "Deliver to",
            address = order.deliveryLocation.address,
            time = order.estimatedDeliveryTime,
            iconColor = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Distance Info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = order.distance,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Text(
                text = "ETA: ${order.estimatedDeliveryTime}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun LocationItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    address: String,
    time: String,
    iconColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(iconColor.copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = address,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Time: $time",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun OrderItemsList(
    items: List<OrderItem>,
    totalAmount: Double,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                RoundedCornerShape(12.dp)
            )
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Order Items (${items.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            IconButton(onClick = onToggle) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand"
                )
            }
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))

                items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${item.quantity}x ${item.name}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "₹${item.price}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total Amount",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "₹$totalAmount",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}


@Composable
fun ActionButtons(
    status: OrderStatus,
    onStartNavigation: () -> Unit,
    onPickupConfirm: () -> Unit,
    onDeliveryConfirm: () -> Unit,
) {

    var isLoading by remember { mutableStateOf(false) }
    // Reset loading state if the order status changes from an external source
    LaunchedEffect(status) {
        isLoading = false
    }

    val onActionClick: (() -> Unit) -> Unit = { action ->
        isLoading = true
        action()
    }

    // Define the base modifier for all buttons
    val buttonModifier = Modifier
        .fillMaxWidth()
        .height(56.dp)

    when (status) {
        OrderStatus.PENDING -> {
            AnimatedActionButton(
                text = "Start Navigation to Restaurant",
                icon = Icons.Default.Navigation,
                onClick = { onActionClick(onStartNavigation) },
                isLoading = isLoading,
                modifier = buttonModifier
            )
        }

        OrderStatus.EN_ROUTE_TO_PICKUP, OrderStatus.ARRIVED_AT_PICKUP -> {
            AnimatedActionButton(
                text = "Confirm Pickup",
                icon = Icons.Default.CheckCircle,
                onClick = { onActionClick(onPickupConfirm) },
                isLoading = isLoading,
                backgroundColor = MaterialTheme.colorScheme.secondary,
                modifier = buttonModifier
            )
        }

        OrderStatus.PICKED_UP, OrderStatus.EN_ROUTE_TO_DELIVERY -> {
            AnimatedActionButton(
                text = "Navigate to Customer",
                icon = Icons.Default.Navigation,
                onClick = { onActionClick(onStartNavigation) }, // Assuming this should re-trigger navigation
                isLoading = isLoading,
                modifier = buttonModifier
            )
        }

        OrderStatus.ARRIVED_AT_DELIVERY -> {
            AnimatedActionButton(
                text = "Complete Delivery",
                icon = Icons.Default.Done,
                onClick = { onActionClick(onDeliveryConfirm) },
                isLoading = isLoading,
                backgroundColor = Color(0xFF4CAF50),
                modifier = buttonModifier
            )
        }

        OrderStatus.DELIVERED -> {
            // The "Delivered" state doesn't need an action button, so it remains a Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Delivery Completed!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    isLoading: Boolean = false,
    isEnabled: Boolean = true
) {
    // Nudge Animation Setup
    val infiniteTransition = rememberInfiniteTransition(label = "NudgeAnimation")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "buttonScale"
    )

    Button(
        onClick = onClick,
        modifier = modifier.scale(if (isLoading || !isEnabled) 1f else scale),
        enabled = isEnabled && !isLoading,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
    ) {
        // Animated Content for Loader
        AnimatedContent(
            targetState = isLoading,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "ActionButtonContent"
        ) { loading ->
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = text,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

