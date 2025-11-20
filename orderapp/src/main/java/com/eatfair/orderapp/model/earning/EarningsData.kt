package com.eatfair.orderapp.model.earning

data class EarningsData(
    val totalOrders: Int,
    val ordersDeclined: Int,
    val totalEarned: Double,
    val totalWithdrawn: Double,
    val successOrdersChart: List<ChartDataPoint>,
    val declinedOrdersChart: List<ChartDataPoint>,
    val totalEarnedChart: List<ChartDataPoint>,
    val totalWithdrawChart: List<ChartDataPoint>,
    val dashboardChart: List<ChartDataPoint>,
    val weeklyBreakdown: List<WeeklyEarning>,
    val withdrawalHistory: List<WithdrawalRecord>
)

data class ChartDataPoint(
    val month: String,
    val monthIndex: Float,
    val value: Float,
    val label: String? = null
)

data class WeeklyEarning(
    val weekNumber: Int,
    val dateRange: String,
    val amount: Double,
    val orderCount: Int
)

data class WithdrawalRecord(
    val id: Int,
    val date: String,
    val amount: Double
)