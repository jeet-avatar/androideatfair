package com.eatfair.orderapp.model.auth

import com.eatfair.orderapp.constants.VehicleType

data class DriverRegistration(
    val fullName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val password: String = "",
    val zipCode: String = "",
    val vehicleType: VehicleType? = null
)

data class Requirement(
    val title: String,
    val description: String,
    val isCompleted: Boolean = true
)