package com.eatfair.orderapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eatfair.orderapp.constants.VehicleType
import com.eatfair.shared.data.local.SessionManager
import com.eatfair.orderapp.model.auth.DriverRegistration
import com.eatfair.orderapp.model.auth.Requirement
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

data class RegistrationUiState(
    val registration: DriverRegistration = DriverRegistration(),
    val requirements: List<Requirement> = listOf(
        Requirement("18+ years old", "Must be at least 18 to drive"),
        Requirement("Valid driver's license", "For car and motorcycle drivers"),
        Requirement("Insurance (if driving)", "Required for vehicle drivers"),
        Requirement("Smartphone", "To receive orders and navigate")
    ),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRegistrationSuccess: Boolean = false,
    val validationErrors: Map<String, String> = emptyMap()
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

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
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val userId = result.user?.uid ?: throw Exception("User ID not found")
                
                val user = result.user
                sessionManager.saveSession(userId, user?.displayName, user?.email)
                _authState.value = AuthState.Authenticated(userId)

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
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val userId = result.user?.uid ?: throw Exception("User ID not found")
                
                // Update user profile with name
                val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
                result.user?.updateProfile(profileUpdates)?.await()

                sessionManager.saveSession(userId, name, email)
                _authState.value = AuthState.Authenticated(userId)
            } catch (e: Exception) {
                sessionManager.clearSession()
                _authState.value = AuthState.Error(e.message ?: "Sign up failed")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
            _authState.value = AuthState.UnAuthenticated
        }
    }



    fun updateFullName(name: String) {
        _uiState.value = _uiState.value.copy(
            registration = _uiState.value.registration.copy(fullName = name),
            validationErrors = _uiState.value.validationErrors - "fullName"
        )
    }

    fun updatePhoneNumber(phone: String) {
        _uiState.value = _uiState.value.copy(
            registration = _uiState.value.registration.copy(phoneNumber = phone),
            validationErrors = _uiState.value.validationErrors - "phoneNumber"
        )
    }

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(
            registration = _uiState.value.registration.copy(email = email),
            validationErrors = _uiState.value.validationErrors - "email"
        )
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(
            registration = _uiState.value.registration.copy(password = password),
            validationErrors = _uiState.value.validationErrors - "password"
        )
    }

    fun updateZipCode(zipCode: String) {
        _uiState.value = _uiState.value.copy(
            registration = _uiState.value.registration.copy(zipCode = zipCode),
            validationErrors = _uiState.value.validationErrors - "zipCode"
        )
    }

    fun updateVehicleType(vehicleType: VehicleType) {
        _uiState.value = _uiState.value.copy(
            registration = _uiState.value.registration.copy(vehicleType = vehicleType),
            validationErrors = _uiState.value.validationErrors - "vehicleType"
        )
    }

    private fun validateForm(): Boolean {
        val errors = mutableMapOf<String, String>()
        val reg = _uiState.value.registration

        if (reg.fullName.isBlank()) {
            errors["fullName"] = "Full name is required"
        }
        if (reg.phoneNumber.isBlank()) {
            errors["phoneNumber"] = "Phone number is required"
        }
        if (reg.email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(reg.email).matches()) {
            errors["email"] = "Valid email is required"
        }
        if (reg.password.length < 6) {
            errors["password"] = "Password must be at least 6 characters"
        }
        if (reg.zipCode.isBlank()) {
            errors["zipCode"] = "Zip code is required"
        }
        if (reg.vehicleType == null) {
            errors["vehicleType"] = "Vehicle type is required"
        }

        _uiState.value = _uiState.value.copy(validationErrors = errors)
        return errors.isEmpty()
    }

    fun registerDriver() {
        if (!validateForm()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val reg = _uiState.value.registration
                val result = firebaseAuth.createUserWithEmailAndPassword(reg.email, reg.password).await()
                val userId = result.user?.uid ?: throw Exception("User ID not found")
                
                // Update profile
                val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                    .setDisplayName(reg.fullName)
                    .build()
                result.user?.updateProfile(profileUpdates)?.await()
                
                // Save to Firestore (driver details)
                val driverData = hashMapOf(
                    "fullName" to reg.fullName,
                    "email" to reg.email,
                    "phoneNumber" to reg.phoneNumber,
                    "zipCode" to reg.zipCode,
                    "vehicleType" to reg.vehicleType?.name,
                    "role" to "driver",
                    "createdAt" to com.google.firebase.Timestamp.now()
                )
                
                com.google.firebase.firestore.FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(userId)
                    .set(driverData)
                    .await()

                sessionManager.saveSession(userId, reg.fullName, reg.email)
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRegistrationSuccess = true
                )
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "Registration failed"
                )
            }
        }
    }

}