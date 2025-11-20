package com.eatfair.orderapp.data.repo.impl

import com.eatfair.orderapp.data.repo.AuthRepo
import com.eatfair.orderapp.model.auth.DriverRegistration
import kotlinx.coroutines.delay
import javax.inject.Inject

class AuthRepoImpl @Inject constructor() : AuthRepo {

    override suspend fun login(): Result<Unit> {
        delay(1500)
        return Result.success(Unit)
    }

    override suspend fun register(form: DriverRegistration): Result<Unit> {
        delay(1500)
        return Result.success(Unit)
    }
}