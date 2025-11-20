package com.eatfair.orderapp.di

import com.eatfair.orderapp.data.repo.AuthRepo
import com.eatfair.orderapp.data.repo.DeliveryRepo
import com.eatfair.orderapp.data.repo.EarningsRepo
import com.eatfair.orderapp.data.repo.OrdersRepo
import com.eatfair.orderapp.data.repo.ProfileRepo
import com.eatfair.orderapp.data.repo.impl.AuthRepoImpl
import com.eatfair.orderapp.data.repo.impl.DeliveryRepoImpl
import com.eatfair.orderapp.data.repo.impl.EarningsRepoImpl
import com.eatfair.orderapp.data.repo.impl.OrdersRepoImpl
import com.eatfair.orderapp.data.repo.impl.ProfileRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    @Singleton
    abstract fun provideAuthRepo(authRepoImpl: AuthRepoImpl): AuthRepo

    @Binds
    @Singleton
    abstract fun provideDeliveryRepo(deliveryRepoImpl: DeliveryRepoImpl): DeliveryRepo

    @Binds
    @Singleton
    abstract fun provideOrderRepo(orderRepoImpl: OrdersRepoImpl): OrdersRepo

    @Binds
    @Singleton
    abstract fun provideProfileRepo(profileRepoImpl: ProfileRepoImpl): ProfileRepo

    @Binds
    @Singleton
    abstract fun provideEarningsRepo(earningsRepoImpl: EarningsRepoImpl): EarningsRepo

}