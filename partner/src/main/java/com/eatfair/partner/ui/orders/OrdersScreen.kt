package com.eatfair.partner.ui.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eatfair.shared.constants.OrderStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    viewModel: OrdersViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onOrderClick: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("All", "Pending", "Preparing", "Out for Delivery", "Delivered")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Orders",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // Tab Row
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.primary,
                edgePadding = 16.dp
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { 
                            Text(
                                title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            ) 
                        }
                    )
                }
            }
            
            // Orders List
            val filteredOrders = when (selectedTab) {
                0 -> uiState.orders
                1 -> uiState.orders.filter { it.status == OrderStatus.ORDER_PLACED }
                2 -> uiState.orders.filter { it.status == OrderStatus.PREPARING }
                3 -> uiState.orders.filter { it.status == OrderStatus.OUT_FOR_DELIVERY }
                4 -> uiState.orders.filter { it.status == OrderStatus.DELIVERED }
                else -> uiState.orders
            }
            
            if (filteredOrders.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No orders found",
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredOrders) { order ->
                        OrderItemCard(
                            order = order,
                            onClick = { onOrderClick(order.id) },
                            onAccept = { viewModel.acceptOrder(order.id) },
                            onReject = { viewModel.rejectOrder(order.id) },
                            onMarkPreparing = { viewModel.markAsPreparing(order.id) },
                            onMarkReady = { viewModel.markAsReady(order.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItemCard(
    order: OrderItem,
    onClick: () -> Unit,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onMarkPreparing: () -> Unit,
    onMarkReady: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Order #${order.id}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = order.customerName,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = when (order.status) {
                        OrderStatus.ORDER_PLACED -> com.eatfair.partner.ui.theme.OrderPlacedBg
                        OrderStatus.PREPARING -> com.eatfair.partner.ui.theme.OrderPreparingBg
                        OrderStatus.OUT_FOR_DELIVERY -> com.eatfair.partner.ui.theme.OrderReadyBg
                        OrderStatus.DELIVERED -> com.eatfair.partner.ui.theme.OrderDeliveredBg
                    }
                ) {
                    Text(
                        text = order.status.name.replace("_", " "),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = when (order.status) {
                            OrderStatus.ORDER_PLACED -> com.eatfair.partner.ui.theme.OrderPlaced
                            OrderStatus.PREPARING -> com.eatfair.partner.ui.theme.OrderPreparing
                            OrderStatus.OUT_FOR_DELIVERY -> com.eatfair.partner.ui.theme.OrderReady
                            OrderStatus.DELIVERED -> com.eatfair.partner.ui.theme.OrderDelivered
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Order Items
            Text(
                text = "Items:",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            order.items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${item.quantity}x ${item.name}",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "$${item.price * item.quantity}",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))
            
            // Total and Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = order.time,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                
                Text(
                    text = "Total: $${order.totalAmount}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            // Action Buttons
            when (order.status) {
                OrderStatus.ORDER_PLACED -> {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onReject,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Red
                            )
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Reject")
                        }
                        
                        Button(
                            onClick = onAccept,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Accept")
                        }
                    }
                }
                OrderStatus.PREPARING -> {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onMarkReady,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Mark as Ready")
                    }
                }
                else -> {}
            }
        }
    }
}

data class OrderItem(
    val id: Long,
    val customerName: String,
    val customerPhone: String,
    val items: List<OrderMenuItem>,
    val totalAmount: Double,
    val status: OrderStatus,
    val time: String,
    val deliveryAddress: String
)

data class OrderMenuItem(
    val name: String,
    val quantity: Int,
    val price: Double
)
