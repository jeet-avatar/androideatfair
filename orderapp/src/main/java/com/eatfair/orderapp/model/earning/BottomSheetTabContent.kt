package com.eatfair.orderapp.model.earning

data class BottomSheetTabContent(
    val title: String,
    val description: String,
    val chartData: List<ChartDataPoint>,
    val weeklyData: List<WeeklyEarning>
)