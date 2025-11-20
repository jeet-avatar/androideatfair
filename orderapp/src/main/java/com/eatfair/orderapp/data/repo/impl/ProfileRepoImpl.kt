package com.eatfair.orderapp.data.repo.impl

import com.eatfair.orderapp.data.repo.ProfileRepo
import com.eatfair.orderapp.model.AppSettings
import com.eatfair.orderapp.model.FAQItem
import com.eatfair.orderapp.model.profile.UserProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProfileRepoImpl @Inject constructor(

) : ProfileRepo {

    private var currentProfile = UserProfile(
        username = "Marcus Stooke",
        email = "marcusstooke28@gmail.com",
        driverLicenseNumber = "D1234567",
        driverLicenseImageUrl = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/27/d5/bb/74/lounge.jpg?w=900&h=500&s=1",
        socialSecurityNumber = "XXX-XX-1234"
    )

    private var currentSettings = AppSettings()

    override suspend fun getUserProfile(): UserProfile {
        delay(500)
        return currentProfile
    }

    override suspend fun updateProfile(profile: UserProfile): Result<Unit> {
        delay(800)
        currentProfile = profile
        return Result.success(Unit)
    }

    override suspend fun updatePassword(oldPassword: String, newPassword: String): Result<Unit> {
        delay(1000)
        // Simulate password validation
        return if (oldPassword.length >= 8 && newPassword.length >= 8) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Password must be at least 8 characters"))
        }
    }

    override suspend fun getAppSettings(): AppSettings {
        delay(400)
        return currentSettings
    }

    override suspend fun updateSettings(settings: AppSettings): Result<Unit> {
        delay(500)
        currentSettings = settings
        return Result.success(Unit)
    }

    override fun getFAQs(): Flow<List<FAQItem>> = flow {
        delay(600)
        emit(
            listOf(
                FAQItem(
                    question = "How do I become a delivery partner?",
                    answer = "To become a delivery partner, you need to be at least 18 years old, have a valid driver's license, vehicle registration, and insurance. Complete the registration process in the app."
                ),
                FAQItem(
                    question = "How will I receive payments?",
                    answer = "Payments are processed weekly and deposited directly into your bank account. You can track your earnings in the app and view payment history in the Orders section."
                ),
                FAQItem(
                    question = "What documents are required to sign up?",
                    answer = "You'll need a valid driver's license, Social Security Number for tax purposes, vehicle registration, and insurance. Some locations may require a background check."
                ),
                FAQItem(
                    question = "How is my rating calculated?",
                    answer = "Your rating is based on customer feedback, on-time deliveries, order accuracy, and professionalism. Maintaining a high rating can lead to more delivery opportunities."
                ),
                FAQItem(
                    question = "What happens if I cancel an order?",
                    answer = "Canceling accepted orders may affect your rating and acceptance rate. Only cancel if absolutely necessary, and contact support if you have issues."
                ),
                FAQItem(
                    question = "Are there bonuses or incentives?",
                    answer = "Yes! We offer peak hour bonuses, weekly incentives for completing a certain number of deliveries, and special promotions. Check the Earnings section regularly."
                )
            )
        )
    }
}