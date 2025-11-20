package com.eatfair.orderapp.ui.screens.earnings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eatfair.orderapp.constants.EarningsFilterType
import com.eatfair.orderapp.data.repo.EarningsRepo
import com.eatfair.orderapp.model.earning.EarningsData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class EarningsUiState {
    object Loading : EarningsUiState()
    data class Success(val data: EarningsData) : EarningsUiState()
    data class Error(val message: String) : EarningsUiState()
}

@HiltViewModel
class EarningsViewModel @Inject constructor(
    private val earningsRepo: EarningsRepo
): ViewModel() {

    private val _uiState = MutableStateFlow<EarningsUiState>(EarningsUiState.Loading)
    val uiState: StateFlow<EarningsUiState> = _uiState.asStateFlow()

    private val _isSheetVisible = MutableStateFlow(false)
    val isSheetVisible = _isSheetVisible.asStateFlow()

    // 2. State to control which filter/tab is selected
    private val _selectedFilter = MutableStateFlow(EarningsFilterType.SUCCESS_ORDERS)
    val selectedFilter = _selectedFilter.asStateFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _uiState.value = EarningsUiState.Loading
            try {
                val data = earningsRepo.getDashboardData()
                _uiState.value = EarningsUiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = EarningsUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun showBottomSheet(filterType: EarningsFilterType) {
        _selectedFilter.value = filterType
        _isSheetVisible.value = true
    }

    fun hideBottomSheet() {
        _isSheetVisible.value = false
    }

    fun onFilterChanged(newFilterType: EarningsFilterType) {
        _selectedFilter.value = newFilterType
    }
}