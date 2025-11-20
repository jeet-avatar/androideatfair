package com.eatfair.app.ui.home

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eatfair.shared.data.local.SessionManager
import com.eatfair.shared.data.repo.AddressRepo
import com.eatfair.shared.data.repo.RestaurantRepo
import com.eatfair.shared.model.address.AddressDto
import com.eatfair.shared.model.home.CarouselItem
import com.eatfair.shared.model.home.Category
import com.eatfair.shared.model.home.FoodItem
import com.eatfair.shared.model.restaurant.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val addressRepo: AddressRepo,
    private val restaurantRepo: RestaurantRepo
) : ViewModel() {

    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri = _profileImageUri.asStateFlow()

    private val _carouselItems = MutableStateFlow<List<CarouselItem>>(emptyList())
    val carouselItems: StateFlow<List<CarouselItem>> = _carouselItems.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _topRatedFood = MutableStateFlow<List<FoodItem>>(emptyList())
    val topRatedFood: StateFlow<List<FoodItem>> = _topRatedFood.asStateFlow()

    private val _restaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val restaurants: StateFlow<List<Restaurant>> = _restaurants.asStateFlow()

    private val _defaultAddress = MutableStateFlow<AddressDto?>(null)
    val defaultAddress = _defaultAddress.asStateFlow()

    init {
        checkProfileImage()
        fetchCarouselItems()
        fetchCategories()
        fetchTopRatedFood()
        fetchRestaurants()

        viewModelScope.launch {
            fetchDefaultAddress()
        }
    }

    suspend fun fetchDefaultAddress() {
        val defaultAddress = addressRepo.getDefaultAddress()
        _defaultAddress.value = defaultAddress
    }


    private fun checkProfileImage() {
        sessionManager.profileImageUriFlow
            .onEach { uriString ->
                _profileImageUri.value = uriString?.toUri()
            }
            .launchIn(viewModelScope)
    }

    private fun fetchTopRatedFood() {
        viewModelScope.launch {
            _topRatedFood.value = restaurantRepo.getTopRatedFood().first()
        }
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            _categories.value = restaurantRepo.getCategories().first()
        }
    }

    private fun fetchCarouselItems() {
        viewModelScope.launch {
            _carouselItems.value = restaurantRepo.getCarouselItems().first()
        }
    }


    fun fetchRestaurants() {
        viewModelScope.launch {
            _restaurants.value = restaurantRepo.getRestaurants().first()
        }
    }

}