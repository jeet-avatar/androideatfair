package com.eatfair.orderapp.data.repo

import com.eatfair.orderapp.model.auth.DriverRegistration

interface AuthRepo {

    suspend fun login(): Result<Unit>

    suspend fun register(form: DriverRegistration): Result<Unit>
}