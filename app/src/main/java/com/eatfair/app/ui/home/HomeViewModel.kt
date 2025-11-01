package com.eatfair.app.ui.home

import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eatfair.app.data.SessionManager
import com.eatfair.app.data.repo.AddressRepo
import com.eatfair.app.data.repo.RestaurantRepo
import com.eatfair.app.model.address.AddressDto
import com.eatfair.app.model.home.CarouselItem
import com.eatfair.app.model.home.Category
import com.eatfair.app.model.home.FoodItem
import com.eatfair.app.model.restaurant.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    val carouselItemsSample = listOf(
        CarouselItem(
            id = 1,
            title = "Flavors in Karbooz",
            subtitle = "Spice Up Your Day",
            description = "Taste Mexico!\nCraving Bold Mexican Flavors?",
            backgroundColor = Color(0xFFFFB6D9)
        ),
        CarouselItem(
            id = 2,
            title = "Asian Delights",
            subtitle = "Experience The East",
            description = "Authentic Asian\nCuisine Awaits!",
            backgroundColor = Color(0xFFB6E5FF)
        ),
        CarouselItem(
            id = 3,
            title = "Italian Classics",
            subtitle = "Taste of Italy",
            description = "Fresh Pasta &\nPizza Daily!",
            backgroundColor = Color(0xFFFFE5B6)
        )
    )

    val categoriesSample = listOf(
        Category(1, "Tacos", "🌮", true),
        Category(2, "Zephyr", "🧁"),
        Category(3, "Burgers", "🍔"),
        Category(4, "Beauty", "🍜"),
        Category(5, "Pizza", "🍕"),
        Category(6, "Sushi", "🍱"),
        Category(7, "Dessert", "🍰")
    )

    val topRatedFoodSample = listOf(
        FoodItem(1, "Chicken Tacos", 12.99, 4.8),
        FoodItem(2, "Beef Burrito", 12.99, 4.7),
        FoodItem(3, "Veggie Bowl", 10.99, 4.9),
        FoodItem(4, "Fish Tacos", 13.99, 4.6)
    )

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
        _topRatedFood.value = topRatedFoodSample
    }

    private fun fetchCategories() {
        _categories.value = categoriesSample
    }

    private fun fetchCarouselItems() {
        _carouselItems.value = carouselItemsSample
    }


    fun fetchRestaurants() {
        viewModelScope.launch {
            restaurantRepo.getRestaurants().collect {
                _restaurants.value = it
            }
        }
    }

}