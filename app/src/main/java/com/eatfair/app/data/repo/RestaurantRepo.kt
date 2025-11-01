package com.eatfair.app.data.repo

import com.eatfair.app.data.FakeDataSource
import com.eatfair.app.model.home.Category
import com.eatfair.app.model.restaurant.MenuItem
import com.eatfair.app.model.restaurant.Restaurant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

// This repository will be the single source of truth for restaurant-related data.
@Singleton
class RestaurantRepo @Inject constructor(
    private val fakeDataSource: FakeDataSource // Hilt injects the fake data source here
) {

    // Using Flow to simulate an asynchronous data stream (like from a network or database).
    fun getRestaurants(): Flow<List<Restaurant>> = flow {
        emit(fakeDataSource.getRestaurants())
    }

    fun getRestaurantById(id: Int): Flow<Restaurant?> = flow {
        emit(fakeDataSource.getRestaurantById(id))
    }

    fun getMenuItems(restaurantId: Int): Flow<List<MenuItem>> = flow {
        emit(fakeDataSource.getMenuItemsForRestaurant(restaurantId))
    }

    fun getCategories(restaurantId: Int): Flow<List<Category>> = flow {
        emit(fakeDataSource.getCategoriesForRestaurant(restaurantId))
    }
}