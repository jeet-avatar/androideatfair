package com.eatfair.shared.data.repo

import androidx.compose.ui.graphics.Color
import com.eatfair.shared.model.home.CarouselItem
import com.eatfair.shared.model.home.Category
import com.eatfair.shared.model.home.FoodItem
import com.eatfair.shared.model.restaurant.MenuItem
import com.eatfair.shared.model.restaurant.Restaurant
import com.eatfair.shared.model.search.SearchResultDto
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantRepo @Inject constructor(
    private val firestore: com.google.firebase.firestore.FirebaseFirestore
) {

    // region Hardcoded Data
    private val allRestaurants = listOf(
        Restaurant(
            id = 12,
            name = "Natraj Restaurant",
            rating = 4.5f,
            reviews = "1.2K+",
            distance = "1.0 km",
            deliveryTime = "20-25 mins",
            cuisineType = "Indian",
            isPureVeg = false,
            isOpen = true,
            tags = listOf("Featured", "Indian"),
            imageUrl = "https://s3-media1.fl.yelpcdn.com/bphoto/A5Ky-6s0kl9i9c3v3p_OIA/o.jpg",
            address = "22205 El Paseo #A, Rancho Santa Margarita, CA 92688",
            latitude = 33.6402,
            longitude = -117.6033
        ),
        Restaurant(
            id = 2,
            name = "Desi Laddu House",
            rating = 4.2f,
            reviews = "5.2K+",
            distance = "3.5 km",
            deliveryTime = "30-35 mins",
            cuisineType = "Sweets, Indian",
            isPureVeg = true,
            isOpen = true,
            tags = listOf("Popular", "Pure Veg"),
            imageUrl = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/27/d5/bb/74/lounge.jpg?w=900&h=500&s=1",
            address = "14, Chandni Chowk Market, Delhi",
            latitude = 28.6508,
            longitude = 77.2300
        ),
    )

    private val allMenuItems = listOf(
        // Natraj Restaurant Menu
        MenuItem(
            87,
            "Garlic Naan",
            5.00f,
            "Leavened white bread topped with garlic & cilantro baked to order in the tandoor...",
            true,
            true,
            true,
            100,
            "https://s3-media1.fl.yelpcdn.com/bphoto/A5Ky-6s0kl9i9c3v3p_OIA/o.jpg",
            12
        ),
        MenuItem(
            88,
            "Chicken Tikka Masala",
            24.00f,
            "Everybody's Favorite! Tandoori roasted breast of chicken in a velvety sauce of...",
            false,
            true,
            false,
            101,
            "https://s3-media1.fl.yelpcdn.com/bphoto/A5Ky-6s0kl9i9c3v3p_OIA/o.jpg",
            12
        ),
        MenuItem(
            89,
            "Saffron Rice",
            5.00f,
            "",
            true,
            false,
            false,
            102,
            "https://s3-media1.fl.yelpcdn.com/bphoto/A5Ky-6s0kl9i9c3v3p_OIA/o.jpg",
            12
        ),
        MenuItem(
            90,
            "Mixed Green Salad",
            8.00f,
            "",
            true,
            false,
            true,
            103,
            "https://s3-media1.fl.yelpcdn.com/bphoto/A5Ky-6s0kl9i9c3v3p_OIA/o.jpg",
            12
        ),
        MenuItem(
            91,
            "Mango Corn Soup",
            -1f, // Price not visible
            "",
            true,
            false,
            false,
            103,
            "https://s3-media1.fl.yelpcdn.com/bphoto/A5Ky-6s0kl9i9c3v3p_OIA/o.jpg",
            12
        ),

        // Desi Laddu House Menu
        MenuItem(
            1,
            "Kaju Barfi",
            42.0f,
            "Indulge in the heavenly sweetness of kaju barfi...",
            true,
            true,
            true,
            1,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/Kaju_katli_sweet.jpg/1200px-Kaju_katli_sweet.jpg",
            2
        ),
    )

    private val allCategories = listOf(
        // Natraj Restaurant Categories
        Category(100, "Featured Items", "⭐", true),
        Category(101, "Tandoori Specialties", "🔥", true),
        Category(102, "Rice", "🍚"),
        Category(103, "Appetizers", "🥗"),

        // Desi Laddu House (ID 2)
        Category(1, "Barfi", "🔶", true),
    )

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

    val topRatedFoodSample = listOf(
        FoodItem(1, "Chicken Tacos", 12.99, 4.8),
        FoodItem(2, "Beef Burrito", 12.99, 4.7),
        FoodItem(3, "Veggie Bowl", 10.99, 4.9),
        FoodItem(4, "Fish Tacos", 13.99, 4.6)
    )

    val recentSearches = listOf(
        "Italian Pizza",
        "Burger King",
        "Vegetarian",
        "Dessert",
        "Salad",
        "Pancakes"
    )

    val popularCuisines = listOf(
        "Breakfast",
        "Snack",
        "Fast Food",
        "Beverages",
        "International",
        "Rice",
        "Noodles",
        "Chicken",
        "Seafood"
    )

    val fakeRestaurantResults = listOf(
        SearchResultDto(
            id = 1,
            name = "The Golden Spoon",
            category = "Breakfast",
            rating = 4.8,
            imageUrl = "https://example.com/images/golden_spoon.jpg"
        ),
        SearchResultDto(
            id = 2,
            name = "Mama Rosa’s Trattoria",
            category = "Italian",
            rating = 4.6,
            imageUrl = "https://example.com/images/mama_rosa.jpg"
        ),
        SearchResultDto(
            id = 3,
            name = "Burger Hub",
            category = "Fast Food",
            rating = 4.3,
            imageUrl = "https://example.com/images/burger_hub.jpg"
        ),
    )

    // endregion

    fun getRestaurants(): Flow<List<Restaurant>> = flow {
        // Try fetching from Firestore
        try {
            val snapshot = firestore.collection("restaurants").get().await()
            val restaurants = snapshot.toObjects(Restaurant::class.java)
            if (restaurants.isNotEmpty()) {
                emit(restaurants)
            } else {
                emit(allRestaurants)
            }
        } catch (e: Exception) {
            // Fallback to dummy data on error
            emit(allRestaurants)
        }
    }

    fun getRestaurantById(id: Int): Restaurant? = allRestaurants.find { it.id == id }

    fun getMenuItemsForRestaurant(restaurantId: Int): List<MenuItem> {
        return allMenuItems.filter { it.restaurantId == restaurantId }
    }

    fun getCategories(): Flow<List<Category>> = flow {
        emit(allCategories)
    }

    fun getCategoriesForRestaurant(restaurantId: Int): List<Category> {
        val categoryIdsForRestaurant =
            getMenuItemsForRestaurant(restaurantId).map { it.categoryId }.toSet()
        return allCategories.filter { it.id in categoryIdsForRestaurant }
    }

    fun getCarouselItems(): Flow<List<CarouselItem>> = flow {
        emit(carouselItemsSample)
    }

    fun getTopRatedFood(): Flow<List<FoodItem>> = flow {
        emit(topRatedFoodSample)
    }

    fun getRecentSearches(): Flow<List<String>> = flow {
        emit(recentSearches)
    }

    fun getPopularCuisines(): Flow<List<String>> = flow {
        emit(popularCuisines)
    }

    fun getSearchResults(query: String): Flow<List<SearchResultDto>> = flow {
        val results = if (query.isBlank()) {
            emptyList()
        } else {
            fakeRestaurantResults.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.category.contains(query, ignoreCase = true)
            }
        }
        emit(results)
    }
}
