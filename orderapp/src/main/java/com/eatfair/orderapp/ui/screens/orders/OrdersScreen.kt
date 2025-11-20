package com.eatfair.orderapp.ui.screens.orders

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eatfair.orderapp.constants.OrderHistoryStatus
import com.eatfair.orderapp.model.order.EarningsSummary
import com.eatfair.orderapp.model.order.OrderHistory
import com.eatfair.orderapp.model.order.OrderHistoryItem
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    onNavigateToDetail: (String) -> Unit,
    viewModel: OrdersViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                // Top App Bar
                TopAppBar(
                    title = {
                        Text(
                            text = "Order History",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF333333)
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )

                // Earnings Summary
                when (uiState) {
                    is OrdersUiState.Success -> {
                        val earnings = (uiState as OrdersUiState.Success).earnings
                        EarningsSummaryCard(earnings = earnings)
                    }

                    else -> {
                        EarningsSummaryCard(
                            earnings = EarningsSummary(0.0, 0.0)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { viewModel.searchOrders(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Header Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "All Orders",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF333333)
                    )
                    Text(
                        text = "Date",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF999999)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        },
        containerColor = Color(0xFFFAFAFA)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is OrdersUiState.Loading -> {
                    LoadingOrdersScreen()
                }

                is OrdersUiState.Success -> {
                    OrdersList(
                        orders = state.orders,
                        onOrderClick = onNavigateToDetail
                    )
                }

                is OrdersUiState.Empty -> {
                    EmptyOrdersScreen()
                }

                is OrdersUiState.Error -> {
                    ErrorOrdersScreen(
                        message = state.message,
                        onRetry = { viewModel.loadOrders() }
                    )
                }
            }
        }
    }
}

@Composable
fun EarningsSummaryCard(earnings: EarningsSummary) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Balance",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF999999),
                fontSize = 13.sp
            )
            Text(
                text = String.format(Locale.US, "%.2f", earnings.balance),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "Total Earned",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF999999),
                fontSize = 13.sp
            )
            Text(
                text = String.format(Locale.US, "%.2f", earnings.totalEarned),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                fontSize = 24.sp
            )
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        placeholder = {
            Text(
                text = "Search here",
                color = Color(0xFFBBBBBB),
                fontSize = 15.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color(0xFFBBBBBB)
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFF5F5F5),
            focusedContainerColor = Color(0xFFF5F5F5),
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}

@Composable
fun OrdersList(
    orders: List<OrderHistory>,
    onOrderClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(orders) { order ->
            OrderHistoryCard(
                order = order,
                onClick = { onOrderClick(order.orderId) }
            )
        }
    }
}

@Composable
fun OrderHistoryCard(
    order: OrderHistory,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(width = if (order.status == OrderHistoryStatus.COMPLETED) 0.dp else 2.dp, color = when (order.status) {
            OrderHistoryStatus.CANCELLED -> MaterialTheme.colorScheme.errorContainer
            OrderHistoryStatus.IN_PROGRESS -> MaterialTheme.colorScheme.primaryContainer
            OrderHistoryStatus.COMPLETED -> MaterialTheme.colorScheme.tertiaryContainer
        }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = order.restaurantName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF333333),
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Order No: ${order.orderId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFBBBBBB),
                    fontSize = 13.sp
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${String.format(Locale.US, "%.2f", order.amount)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333),
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = order.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFBBBBBB),
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun EmptyOrdersScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Cancel,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = Color(0xFFFF5252)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "No Orders Yet!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Start your business with us and\nplace your first order",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF999999),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun LoadingOrdersScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color(0xFF7B3F3F)
        )
    }
}

@Composable
fun ErrorOrdersScreen(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = Color(0xFFFF5252)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Error",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF666666)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7B3F3F)
                )
            ) {
                Text("Retry")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun OrderHistoryCardPreview() {
    OrderHistoryCard(
        order = OrderHistory(
            orderId = "30VN15VD57",
            invoiceId = "30VN15VD57",
            restaurantName = "Monicas Trattoria",
            amount = 33.75,
            date = "20 Oct, 2024",
            status = OrderHistoryStatus.COMPLETED,
            customerName = "Sabrina Lorenshtein",
            customerAddress = "155 W 51st St, New York",
            restaurantAddress = "82 W 14th St, New York",
            items = listOf()
        ),
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun OrderDetailItemCardPreview() {
    OrderDetailItemCard(
        item = OrderHistoryItem(
            name = "Chocomocco",
            description = "A cupcake is a small, single-serving cake typically...",
            quantity = 1,
            price = 8.00
        )
    )
}

@Preview(showBackground = true)
@Composable
fun EarningsSummaryPreview() {
    EarningsSummaryCard(
        earnings = EarningsSummary(
            balance = 125.50,
            totalEarned = 1250.75
        )
    )

}