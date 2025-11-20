package com.eatfair.orderapp.ui.screens.earnings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eatfair.orderapp.constants.EarningsFilterType
import com.eatfair.orderapp.model.earning.EarningsData
import com.eatfair.orderapp.ui.screens.earnings.components.EarningsDetailBottomSheet
import com.eatfair.orderapp.ui.screens.earnings.components.LineChartCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EarningsHomeScreen(
    viewModel: EarningsViewModel
) {

    val uiState by viewModel.uiState.collectAsState()
    val isSheetVisible by viewModel.isSheetVisible.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    LaunchedEffect(isSheetVisible) {
        if (isSheetVisible) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    when (val state = uiState) {
        is EarningsUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF7B3F3F))
            }
        }

        is EarningsUiState.Success -> {
            DashboardContent(
                data = state.data,
                onStatCardClick = { filterType ->
                    viewModel.showBottomSheet(filterType)
                },
            )
        }

        is EarningsUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(state.message, color = Color(0xFFFF5252))
            }
        }
    }

    // Earnings Detail Bottom Sheet
    if (isSheetVisible) {
        val data = (uiState as? EarningsUiState.Success)?.data
        if (data != null) {
            EarningsDetailBottomSheet(
                sheetState = sheetState,
                data = data,
                selectedFilter = selectedFilter,
                onFilterChange = { viewModel.onFilterChanged(it) },
                onDismiss = { viewModel.hideBottomSheet() }
            )
        }
    }
}

@Composable
fun DashboardContent(
    data: EarningsData,
    onStatCardClick: (EarningsFilterType) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Dashboard",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "A Food Driver's Dashboard is an intuitive interface designed for food delivery.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF999999),
                    lineHeight = 20.sp
                )
            }
        }

        item {
            LineChartCard(
                title = "Charts",
                dateRange = "Jun 2024/Nov 2024",
                chartData = data.dashboardChart,
                lineColor = Color(0xFF7B3F3F)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Total Orders",
                    value = data.totalOrders.toString(),
                    trend = "↑ 27%",
                    trendColor = Color(0xFF4CAF50),
                    lineColor = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f),
                    onClick = { onStatCardClick(EarningsFilterType.SUCCESS_ORDERS) }
                )

                StatCard(
                    title = "Orders Declined",
                    value = data.ordersDeclined.toString(),
                    trend = "↓ 12%",
                    trendColor = Color(0xFFFF5252),
                    lineColor = Color(0xFFFF5252),
                    modifier = Modifier.weight(1f),
                    onClick = { onStatCardClick(EarningsFilterType.DECLINED_ORDERS) }
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Total Earned",
                    value = "$${String.format("%.0f", data.totalEarned)}",
                    trend = "↑ 18%",
                    trendColor = Color(0xFF4CAF50),
                    lineColor = Color(0xFF2196F3),
                    modifier = Modifier.weight(1f),
                    onClick = { onStatCardClick(EarningsFilterType.TOTAL_EARNED) }
                )

                StatCard(
                    title = "Total Withdraw",
                    value = "$${String.format("%.0f", data.totalWithdrawn)}",
                    trend = "↑ 15%",
                    trendColor = Color(0xFF4CAF50),
                    lineColor = Color(0xFFFF9800),
                    modifier = Modifier.weight(1f),
                    onClick = { onStatCardClick(EarningsFilterType.TOTAL_WITHDRAW) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    trend: String,
    trendColor: Color,
    lineColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF999999),
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = value,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333),
                        fontSize = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = trend,
                style = MaterialTheme.typography.bodySmall,
                color = trendColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Mini chart line placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(lineColor.copy(alpha = 0.1f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .fillMaxHeight()
                        .background(lineColor.copy(alpha = 0.3f))
                )
            }
        }
    }
}