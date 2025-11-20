package com.eatfair.shared.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

// DataStore instance, defined once at the application level
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebaseAuth: com.google.firebase.auth.FirebaseAuth
) {

    // Keys for storing data
    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    private val USER_ID = stringPreferencesKey("user_id")
    private val USER_NAME = stringPreferencesKey("user_name")
    private val USER_EMAIL = stringPreferencesKey("user_email")
    private val profileImageUriKey = stringPreferencesKey("profile_image_uri")

    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_LOGGED_IN] ?: (firebaseAuth.currentUser != null)
        }

    val userId: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ID] ?: firebaseAuth.currentUser?.uid
        }

    val userNameFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_NAME] ?: firebaseAuth.currentUser?.displayName
        }

    val userEmailFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_EMAIL] ?: firebaseAuth.currentUser?.email
        }

    val profileImageUriFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[profileImageUriKey] ?: firebaseAuth.currentUser?.photoUrl?.toString()
        }

    suspend fun saveProfileImageUri(uriString: String?) {
        context.dataStore.edit { preferences ->
            if (uriString != null) {
                preferences[profileImageUriKey] = uriString
            } else {
                preferences.remove(profileImageUriKey)
            }
        }
    }

    suspend fun saveSession(uid: String, name: String?, email: String?) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
            preferences[USER_ID] = uid
            if (name != null) {
                preferences[USER_NAME] = name
            }
            if (email != null) {
                preferences[USER_EMAIL] = email
            }
        }
    }

    suspend fun clearSession() {
        firebaseAuth.signOut()
        context.dataStore.edit { preferences ->
            preferences.clear() // Clears all auth data
        }
    }
}
