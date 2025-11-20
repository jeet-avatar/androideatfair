package com.eatfair.orderapp.model.profile

import com.eatfair.orderapp.constants.FuelType
import com.eatfair.orderapp.constants.VehicleType

data class UserProfile(
    val username: String,
    val email: String,
    val password: String = "••••••••",
    val profileImageUrl: String? = null,
    val driverLicenseNumber: String? = null,
    val driverLicenseImageUrl: String? = null,
    val socialSecurityNumber: String? = null,
    val vehicleRegistration: VehicleInfo? = null
)

data class VehicleInfo(
    val vehicleType: VehicleType,
    val registrationNumber: String,
    val fuelType: FuelType,
    val manufacturingDate: String,
    val imageUrl: String? = null
)