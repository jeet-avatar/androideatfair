package com.eatfair.app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eatfair.shared.data.repo.RestaurantRepo
import com.eatfair.shared.model.search.SearchResultDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val restaurantRepo: RestaurantRepo
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<SearchResultDto>>(emptyList())
    val searchResults: StateFlow<List<SearchResultDto>> = _searchResults.asStateFlow()

    private val _recentSearches = MutableStateFlow<List<String>>(emptyList())
    val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()

    private val _popularCuisines = MutableStateFlow<List<String>>(emptyList())
    val popularCuisines: StateFlow<List<String>> = _popularCuisines.asStateFlow()

    init {
        observeSearchQuery()
        fetchRecentSearches()
        fetchPopularCuisines()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300L) // Waits for 300ms of silence
                .distinctUntilChanged() // Only proceeds if the text is different
                .onEach { query ->
                    _searchResults.value = restaurantRepo.getSearchResults(query).first()
                }
                .launchIn(viewModelScope)
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun searchByTerm(item: String) {
        _searchQuery.value = item
    }

    private fun fetchRecentSearches() {
        viewModelScope.launch {
            _recentSearches.value = restaurantRepo.getRecentSearches().first()
        }
    }

    private fun fetchPopularCuisines() {
        viewModelScope.launch {
            _popularCuisines.value = restaurantRepo.getPopularCuisines().first()
        }
    }
}