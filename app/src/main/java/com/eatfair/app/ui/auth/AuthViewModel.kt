package com.eatfair.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eatfair.shared.data.local.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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
    private val sessionManager: SessionManager,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
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
                // Sign in with Firebase Authentication
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val userId = result.user?.uid ?: throw Exception("User ID is null")
                
                // Fetch user profile from Firestore
                val userDoc = firestore.collection("users").document(userId).get().await()
                val userName = userDoc.getString("name") ?: email
                
                // Save session locally
                sessionManager.saveSession(userId, userName, email)
                _authState.value = AuthState.Authenticated(email)

            } catch (e: Exception) {
                sessionManager.clearSession()
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun signUp(email: String, password: String, name: String, phone: String, zipCode: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                // Create Firebase Authentication user
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val userId = result.user?.uid ?: throw Exception("User ID is null")
                
                // Create user profile in Firestore
                val userProfile = hashMapOf(
                    "userId" to userId,
                    "name" to name,
                    "email" to email,
                    "phone" to phone,
                    "zipCode" to zipCode,
                    "role" to "customer",
                    "createdAt" to com.google.firebase.firestore.FieldValue.serverTimestamp(),
                    "updatedAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
                )
                
                firestore.collection("users").document(userId).set(userProfile).await()

                // Save session locally
                sessionManager.saveSession(userId, name, email)
                _authState.value = AuthState.Authenticated(email)
            } catch (e: Exception) {
                sessionManager.clearSession()
                _authState.value = AuthState.Error(e.message ?: "Signup failed")
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
