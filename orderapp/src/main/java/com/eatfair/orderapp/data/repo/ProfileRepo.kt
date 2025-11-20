package com.eatfair.orderapp.data.repo

import com.eatfair.orderapp.model.AppSettings
import com.eatfair.orderapp.model.FAQItem
import com.eatfair.orderapp.model.profile.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepo {
    suspend fun getUserProfile(): UserProfile
    suspend fun updateProfile(profile: UserProfile): Result<Unit>
    suspend fun updatePassword(oldPassword: String, newPassword: String): Result<Unit>
    suspend fun getAppSettings(): AppSettings
    suspend fun updateSettings(settings: AppSettings): Result<Unit>
    fun getFAQs(): Flow<List<FAQItem>>
}
