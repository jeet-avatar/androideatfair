package com.eatfair.orderapp.data.repo.impl

import com.eatfair.orderapp.data.repo.EarningsRepo
import com.eatfair.orderapp.model.earning.ChartDataPoint
import com.eatfair.orderapp.model.earning.EarningsData
import com.eatfair.orderapp.model.earning.WeeklyEarning
import com.eatfair.orderapp.model.earning.WithdrawalRecord
import kotlinx.coroutines.delay
import javax.inject.Inject

class EarningsRepoImpl @Inject constructor() : EarningsRepo {

    override suspend fun getDashboardData(): EarningsData {
        delay(800)
        return EarningsData(
            totalOrders = 1857,
            ordersDeclined = 63,
            totalEarned = 2299.0,
            totalWithdrawn = 1308.0,
            dashboardChart = listOf(
                ChartDataPoint("Jun", 0f, 35f),
                ChartDataPoint("Jul", 1f, 90f),
                ChartDataPoint("Aug", 2f, 65f, "$2,299"),
                ChartDataPoint("Oct", 3f, 50f),
                ChartDataPoint("Nov", 4f, 85f)
            ),
            successOrdersChart = listOf(
                ChartDataPoint("Jun", 0f, 40f),
                ChartDataPoint("Jul", 1f, 75f, "$390"),
                ChartDataPoint("Aug", 2f, 60f),
                ChartDataPoint("Oct", 3f, 35f),
                ChartDataPoint("Nov", 4f, 85f)
            ),
            declinedOrdersChart = listOf(
                ChartDataPoint("Jun", 0f, 15f),
                ChartDataPoint("Jul", 1f, 65f),
                ChartDataPoint("Aug", 2f, 95f, "$1,979"),
                ChartDataPoint("Oct", 3f, 70f),
                ChartDataPoint("Nov", 4f, 80f)
            ),
            totalEarnedChart = listOf(
                ChartDataPoint("Jun", 0f, 90f),
                ChartDataPoint("Jul", 1f, 85f),
                ChartDataPoint("Aug", 2f, 55f),
                ChartDataPoint("Oct", 3f, 60f, "$749"),
                ChartDataPoint("Nov", 4f, 30f)
            ),
            totalWithdrawChart = listOf(
                ChartDataPoint("Jun", 0f, 25f),
                ChartDataPoint("Jul", 1f, 70f),
                ChartDataPoint("Aug", 2f, 85f),
                ChartDataPoint("Oct", 3f, 90f, "$1,308"),
                ChartDataPoint("Nov", 4f, 80f)
            ),
            weeklyBreakdown = listOf(
                WeeklyEarning(1, "Oct 20 - Oct 26", 0.0, 16),
                WeeklyEarning(2, "Oct 13 - Oct 19", 0.0, 34),
                WeeklyEarning(3, "Oct 6 - Oct 12", 0.0, 21)
            ),
            withdrawalHistory = listOf(
                WithdrawalRecord(1, "May 31", 227.49),
                WithdrawalRecord(2, "May 15", 69.00),
                WithdrawalRecord(3, "April 30", 154.00),
                WithdrawalRecord(4, "April 15", 89.75)
            )
        )
    }
}