package com.eatfair.shared.data.remote

import com.eatfair.shared.model.restaurant.MenuItem
import com.eatfair.shared.model.restaurant.Restaurant
import retrofit2.http.GET
import retrofit2.http.Path

interface RestaurantApiService {

    @GET("restaurants")
    suspend fun getRestaurants(): List<Restaurant>

    @GET("restaurants/{id}")
    suspend fun getRestaurantById(@Path("id") id: Int): Restaurant

    @GET("restaurants/{id}/menu")
    suspend fun getMenuItems(@Path("id") restaurantId: Int): List<MenuItem>
}
