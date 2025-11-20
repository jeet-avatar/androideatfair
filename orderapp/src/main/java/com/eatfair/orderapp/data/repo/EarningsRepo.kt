package com.eatfair.orderapp.data.repo

import com.eatfair.orderapp.model.earning.EarningsData

interface EarningsRepo {
    suspend fun getDashboardData(): EarningsData

}