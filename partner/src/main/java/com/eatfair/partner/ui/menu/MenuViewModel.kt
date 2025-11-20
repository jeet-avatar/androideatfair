package com.eatfair.partner.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eatfair.shared.data.repo.RestaurantRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MenuItemData(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageUrl: String? = null,
    val isAvailable: Boolean = true
)

data class MenuUiState(
    val menuItems: List<MenuItemData> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val restaurantRepo: RestaurantRepo
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()
    
    private var nextId = 1000
    
    init {
        loadMenuItems()
    }
    
    private fun loadMenuItems() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val items = restaurantRepo.getMenuItemsForRestaurant(12) // Hardcoded ID for now
            val categories = restaurantRepo.getCategoriesForRestaurant(12)
            
            val menuItems = items.map { item ->
                val categoryName = categories.find { it.id == item.categoryId }?.name ?: "Other"
                MenuItemData(
                    id = item.id,
                    name = item.name,
                    description = item.description,
                    price = item.price.toDouble(),
                    category = categoryName,
                    imageUrl = item.imageUrl,
                    isAvailable = item.isAvailable
                )
            }
            
            _uiState.update { 
                it.copy(
                    menuItems = menuItems,
                    isLoading = false
                ) 
            }
            
            nextId = (menuItems.maxOfOrNull { it.id } ?: 999) + 1
        }
    }
    
    fun addMenuItem(name: String, description: String, price: Double, category: String) {
        val newItem = MenuItemData(
            id = nextId++,
            name = name,
            description = description,
            price = price,
            category = category,
            isAvailable = true
        )
        
        _uiState.update { state ->
            state.copy(
                menuItems = state.menuItems + newItem
            )
        }
        // TODO: Call repository to add item
    }
    
    fun updateMenuItem(id: Int, name: String, description: String, price: Double, category: String) {
        _uiState.update { state ->
            state.copy(
                menuItems = state.menuItems.map { item ->
                    if (item.id == id) {
                        item.copy(
                            name = name,
                            description = description,
                            price = price,
                            category = category
                        )
                    } else {
                        item
                    }
                }
            )
        }
        // TODO: Call repository to update item
    }
    
    fun deleteItem(id: Int) {
        _uiState.update { state ->
            state.copy(
                menuItems = state.menuItems.filter { it.id != id }
            )
        }
        // TODO: Call repository to delete item
    }
    
    fun toggleAvailability(id: Int) {
        _uiState.update { state ->
            state.copy(
                menuItems = state.menuItems.map { item ->
                    if (item.id == id) {
                        item.copy(isAvailable = !item.isAvailable)
                    } else {
                        item
                    }
                }
            )
        }
        // TODO: Call repository to update availability
    }
}

