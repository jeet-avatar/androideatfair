package com.eatfair.orderapp.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eatfair.orderapp.data.SessionManager
import com.eatfair.orderapp.data.repo.ProfileRepo
import com.eatfair.orderapp.model.AppSettings
import com.eatfair.orderapp.model.FAQItem
import com.eatfair.orderapp.model.profile.UserProfile
import com.eatfair.orderapp.ui.screens.auth.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val profile: UserProfile) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

sealed class SettingsUiState {
    object Loading : SettingsUiState()
    data class Success(val settings: AppSettings) : SettingsUiState()
    data class Error(val message: String) : SettingsUiState()
}

data class PasswordChangeState(
    val oldPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepo: ProfileRepo,
    private val sessionManager: SessionManager
//    private val sessionManager: SessionManager
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val profileState: StateFlow<ProfileUiState> = _profileState.asStateFlow()

    private val _settingsState = MutableStateFlow<SettingsUiState>(SettingsUiState.Loading)
    val settingsState: StateFlow<SettingsUiState> = _settingsState.asStateFlow()

    private val _passwordState = MutableStateFlow(PasswordChangeState())
    val passwordState: StateFlow<PasswordChangeState> = _passwordState.asStateFlow()

    private val _faqList = MutableStateFlow<List<FAQItem>>(emptyList())
    val faqList: StateFlow<List<FAQItem>> = _faqList.asStateFlow()

    init {
        loadProfile()
        loadSettings()
        loadFAQs()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileUiState.Loading
            try {
                val profile = profileRepo.getUserProfile()
                _profileState.value = ProfileUiState.Success(profile)
            } catch (e: Exception) {
                _profileState.value = ProfileUiState.Error(e.message ?: "Failed to load profile")
            }
        }
    }

    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            try {
                profileRepo.updateProfile(profile)
                loadProfile()
            } catch (e: Exception) {
                _profileState.value = ProfileUiState.Error(e.message ?: "Failed to update profile")
            }
        }
    }

    fun loadSettings() {
        viewModelScope.launch {
            _settingsState.value = SettingsUiState.Loading
            try {
                val settings = profileRepo.getAppSettings()
                _settingsState.value = SettingsUiState.Success(settings)
            } catch (e: Exception) {
                _settingsState.value = SettingsUiState.Error(e.message ?: "Failed to load settings")
            }
        }
    }

    fun updateSettings(settings: AppSettings) {
        viewModelScope.launch {
            try {
                profileRepo.updateSettings(settings)
                _settingsState.value = SettingsUiState.Success(settings)
            } catch (e: Exception) {
                _settingsState.value = SettingsUiState.Error(e.message ?: "Failed to update settings")
            }
        }
    }

    fun updatePasswordField(field: String, value: String) {
        _passwordState.value = when (field) {
            "old" -> _passwordState.value.copy(oldPassword = value, error = null)
            "new" -> _passwordState.value.copy(newPassword = value, error = null)
            "confirm" -> _passwordState.value.copy(confirmPassword = value, error = null)
            else -> _passwordState.value
        }
    }

    fun changePassword() {
        val state = _passwordState.value

        if (state.newPassword != state.confirmPassword) {
            _passwordState.value = state.copy(error = "Passwords do not match")
            return
        }

        if (state.newPassword.length < 8) {
            _passwordState.value = state.copy(error = "Password must be at least 8 characters")
            return
        }

        viewModelScope.launch {
            _passwordState.value = state.copy(isLoading = true, error = null)
            try {
                profileRepo.updatePassword(state.oldPassword, state.newPassword)
                _passwordState.value = PasswordChangeState(success = true)
            } catch (e: Exception) {
                _passwordState.value = state.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to change password"
                )
            }
        }
    }

    fun resetPasswordState() {
        _passwordState.value = PasswordChangeState()
    }

    fun loadFAQs() {
        viewModelScope.launch {
            profileRepo.getFAQs().collect { faqs ->
                _faqList.value = faqs
            }
        }
    }

    fun toggleFAQ(index: Int) {
        val updated = _faqList.value.mapIndexed { i, faq ->
            if (i == index) faq.copy(isExpanded = !faq.isExpanded) else faq
        }
        _faqList.value = updated
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
//            _authState.value = AuthState.UnAuthenticated
        }
    }
}