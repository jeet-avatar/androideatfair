package com.eatfair.orderapp.ui.screens.earnings.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eatfair.orderapp.constants.EarningsFilterType
import com.eatfair.orderapp.model.earning.EarningsData
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun EarningsDetailBottomSheet(
    sheetState: SheetState,
    data: EarningsData,
    selectedFilter: EarningsFilterType,
    onFilterChange: (EarningsFilterType) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFFE0E0E0))
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            // Filter Chips
            FilterChipRow(
                selectedFilter = selectedFilter,
                onFilterChange = onFilterChange
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Content based on selected filter
            AnimatedContent(
                targetState = selectedFilter,
                transitionSpec = {
                    (fadeIn() + slideInVertically()).togetherWith(fadeOut() + slideOutVertically())
                },
                label = "filter_content"
            ) { filter ->
                when (filter) {
                    EarningsFilterType.SUCCESS_ORDERS -> {
                        SuccessOrdersContent(data = data)
                    }
                    EarningsFilterType.DECLINED_ORDERS -> {
                        DeclinedOrdersContent(data = data)
                    }
                    EarningsFilterType.TOTAL_EARNED -> {
                        TotalEarnedContent(data = data)
                    }
                    EarningsFilterType.TOTAL_WITHDRAW -> {
                        TotalWithdrawContent(data = data)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun FilterChipRow(
    selectedFilter: EarningsFilterType,
    onFilterChange: (EarningsFilterType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(androidx.compose.foundation.rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedFilter == EarningsFilterType.SUCCESS_ORDERS,
            onClick = { onFilterChange(EarningsFilterType.SUCCESS_ORDERS) },
            label = { Text("Success Orders") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Color(0xFF7B3F3F),
                selectedLabelColor = Color.White,
                containerColor = Color(0xFFF5F5F5),
                labelColor = Color(0xFF666666)
            )
        )

        FilterChip(
            selected = selectedFilter == EarningsFilterType.DECLINED_ORDERS,
            onClick = { onFilterChange(EarningsFilterType.DECLINED_ORDERS) },
            label = { Text("Declined Orders") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Color(0xFF7B3F3F),
                selectedLabelColor = Color.White,
                containerColor = Color(0xFFF5F5F5),
                labelColor = Color(0xFF666666)
            )
        )

        FilterChip(
            selected = selectedFilter == EarningsFilterType.TOTAL_EARNED,
            onClick = { onFilterChange(EarningsFilterType.TOTAL_EARNED) },
            label = { Text("Total Earned") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Color(0xFF7B3F3F),
                selectedLabelColor = Color.White,
                containerColor = Color(0xFFF5F5F5),
                labelColor = Color(0xFF666666)
            )
        )

        FilterChip(
            selected = selectedFilter == EarningsFilterType.TOTAL_WITHDRAW,
            onClick = { onFilterChange(EarningsFilterType.TOTAL_WITHDRAW) },
            label = { Text("Total Withdraw") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Color(0xFF7B3F3F),
                selectedLabelColor = Color.White,
                containerColor = Color(0xFFF5F5F5),
                labelColor = Color(0xFF666666)
            )
        )
    }
}

@Composable
fun SuccessOrdersContent(data: EarningsData) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.heightIn(max = 500.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Success Orders",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "A streamlined feature for food delivery drivers to track and manage completed deliveries.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF999999),
                    lineHeight = 20.sp
                )
            }
        }

        item {
            LineChartCard(
                title = "Orders Delivered",
                dateRange = "Jun 2024/Nov 2024",
                chartData = data.successOrdersChart,
                lineColor = Color(0xFF4CAF50)
            )
        }

        item {
            Text(
                text = "May",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
        }

        items(data.weeklyBreakdown.size) { index ->
            val week = data.weeklyBreakdown[index]
            WeeklyBreakdownItem(
                weekNumber = week.weekNumber,
                dateRange = week.dateRange,
                orderCount = week.orderCount
            )
        }
    }
}

@Composable
fun DeclinedOrdersContent(data: EarningsData) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.heightIn(max = 500.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Orders Declined",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "A feature that allows drivers to manage and track any delivery requests they have chosen.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF999999),
                    lineHeight = 20.sp
                )
            }
        }

        item {
            LineChartCard(
                title = "Orders Declined",
                dateRange = "Jun 2024/Nov 2024",
                chartData = data.declinedOrdersChart,
                lineColor = Color(0xFFFF5252)
            )
        }

        item {
            Text(
                text = "May",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
        }

        items(data.weeklyBreakdown.size) { index ->
            val week = data.weeklyBreakdown[index]
            WeeklyBreakdownItem(
                weekNumber = week.weekNumber,
                dateRange = week.dateRange,
                orderCount = week.orderCount
            )
        }
    }
}

@Composable
fun TotalEarnedContent(data: EarningsData) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.heightIn(max = 500.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Total Earned",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "A comprehensive overview of a driver's earnings from completed deliveries.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF999999),
                    lineHeight = 20.sp
                )
            }
        }

        item {
            LineChartCard(
                title = "Earned",
                dateRange = "Jun 2024/Nov 2024",
                chartData = data.totalEarnedChart,
                lineColor = Color(0xFF2196F3)
            )
        }

        item {
            Text(
                text = "May",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
        }

        items(data.weeklyBreakdown.size) { index ->
            val week = data.weeklyBreakdown[index]
            WeeklyBreakdownItem(
                weekNumber = week.weekNumber,
                dateRange = week.dateRange,
                orderCount = week.orderCount
            )
        }
    }
}

@Composable
fun TotalWithdrawContent(data: EarningsData) {
   LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.heightIn(max = 500.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Total Withdraw",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Total amount of earnings withdrawn by the delivery partner driver over a specified period.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF999999),
                    lineHeight = 20.sp
                )
            }
        }

        item {
            LineChartCard(
                title = "Total Withdraw",
                dateRange = "Jun 2024/Nov 2024",
                chartData = data.totalWithdrawChart,
                lineColor = Color(0xFFFF9800)
            )
        }

        item {
            Text(
                text = "January To May",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
        }

        items(data.withdrawalHistory.size) { index ->
            val withdrawal = data.withdrawalHistory[index]
            WithdrawalItem(
                number = index + 1,
                date = withdrawal.date,
                amount = withdrawal.amount
            )
        }
    }
}

@Composable
fun WeeklyBreakdownItem(
    weekNumber: Int,
    dateRange: String,
    orderCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = weekNumber.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF666666)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = dateRange,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF333333)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = orderCount.toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF999999),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun WithdrawalItem(
    number: Int,
    date: String,
    amount: Double
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF666666)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = date,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF333333)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = String.format(Locale.US,"$%.2f", amount),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF999999),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

