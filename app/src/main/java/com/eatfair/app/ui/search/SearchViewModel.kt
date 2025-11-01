package com.eatfair.app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eatfair.app.model.SearchResultDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<SearchResultDto>>(emptyList())
    val searchResults: StateFlow<List<SearchResultDto>> = _searchResults.asStateFlow()

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
        SearchResultDto(
            id = 4,
            name = "Sushi Zen Garden",
            category = "Japanese",
            rating = 4.9,
            imageUrl = "https://example.com/images/sushi_zen_garden.jpg"
        ),
        SearchResultDto(
            id = 5,
            name = "Taco Fiesta",
            category = "Mexican",
            rating = 4.5,
            imageUrl = "https://example.com/images/taco_fiesta.jpg"
        ),
        SearchResultDto(
            id = 6,
            name = "Curry Palace",
            category = "Indian",
            rating = 4.7,
            imageUrl = "https://example.com/images/curry_palace.jpg"
        ),
        SearchResultDto(
            id = 7,
            name = "BBQ Brothers",
            category = "Grill & Barbecue",
            rating = 4.4,
            imageUrl = "https://example.com/images/bbq_brothers.jpg"
        ),
        SearchResultDto(
            id = 8,
            name = "Vegan Vibes",
            category = "Vegan & Healthy",
            rating = 4.6,
            imageUrl = "https://example.com/images/vegan_vibes.jpg"
        ),
        SearchResultDto(
            id = 9,
            name = "The Pancake Corner",
            category = "Breakfast & Brunch",
            rating = 4.3,
            imageUrl = "https://example.com/images/pancake_corner.jpg"
        ),
        SearchResultDto(
            id = 10,
            name = "Noodle Nook",
            category = "Asian Fusion",
            rating = 4.5,
            imageUrl = "https://example.com/images/noodle_nook.jpg"
        ),
        SearchResultDto(
            id = 11,
            name = "Spice Route",
            category = "Thai",
            rating = 4.8,
            imageUrl = "https://example.com/images/spice_route.jpg"
        ),
        SearchResultDto(
            id = 12,
            name = "The Dessert Jar",
            category = "Bakery & Sweets",
            rating = 4.9,
            imageUrl = "https://example.com/images/dessert_jar.jpg"
        ),
        SearchResultDto(
            id = 13,
            name = "Mediterraneo",
            category = "Mediterranean",
            rating = 4.7,
            imageUrl = "https://example.com/images/mediterraneo.jpg"
        ),
        SearchResultDto(
            id = 14,
            name = "Street Bites",
            category = "Street Food",
            rating = 4.2,
            imageUrl = "https://example.com/images/street_bites.jpg"
        ),
        SearchResultDto(
            id = 15,
            name = "The Coffee Cabin",
            category = "Café",
            rating = 4.6,
            imageUrl = "https://example.com/images/coffee_cabin.jpg"
        )
    )

    init {
        observeSearchQuery()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        _searchQuery
            .debounce(300L) // Waits for 300ms of silence
            .distinctUntilChanged() // Only proceeds if the text is different
            .onEach { query ->
                // This block will only run when the user has stopped typing for 300ms
                // and the query is new.
                val filteredResults = if (query.isBlank()) {
                    emptyList()
                } else {
                    // This is where you would make your real API call
                    // For now, we filter the fake list.
                    fakeRestaurantResults.filter {
                        it.name.contains(query, ignoreCase = true) ||
                                it.category.contains(query, ignoreCase = true)
                    }
                }
                _searchResults.value = filteredResults
            }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

//    fun search(query: String) {
//        // Perform search logic here
//        // Update search results in the ViewModel
//
//        val filteredResults = fakeRestaurantResults.filter {
//            it.name.contains(query, ignoreCase = true) ||
//                    it.category.contains(query, ignoreCase = true)
//        }
//
//        // Update the search results in the ViewModel
//        _searchResults.value = filteredResults
//
//    }

    fun searchByTerm(item: String) {
        _searchQuery.value = item
    }
}