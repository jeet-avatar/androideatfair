package com.eatfair.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eatfair.app.data.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    object UnAuthenticated : AuthState()
    data class Authenticated(val userId: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {

        viewModelScope.launch {
            sessionManager.isLoggedIn.collect { isLoggedIn ->
                println("Is logged in: $isLoggedIn")
                if (isLoggedIn) {
                    _authState.value = AuthState.Authenticated("manish_id")
                } else {
                    _authState.value = AuthState.UnAuthenticated
                }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                // val result = authRepository.login(email, password)

                // Simulate API call
                delay(1000)

                // On success
                sessionManager.saveSession("Manish Id")
                _authState.value = AuthState.Authenticated("manish_id")

            } catch (e: Exception) {

                sessionManager.clearSession()
                _authState.value = AuthState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun signUp(email: String, password: String, name: String, phone: String, zipCode: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                // val result = authRepository.signUp(email, password, name, phone, zipCode)

                // Simulate API call
                delay(1000)

                // On success
                sessionManager.saveSession("Manish Id")
                _authState.value = AuthState.Authenticated("manish_id")
            } catch (e: Exception) {
                sessionManager.clearSession()
                _authState.value = AuthState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
            _authState.value = AuthState.UnAuthenticated
        }
    }
}
