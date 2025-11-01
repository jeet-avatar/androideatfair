package com.eatfair.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStore instance, defined once at the application level
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class SessionManager(private val context: Context) {

    // Keys for storing data
    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    private val USER_ID = stringPreferencesKey("user_id")
    private val profileImageUriKey = stringPreferencesKey("profile_image_uri")

    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }

    val userId: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ID]
        }


    val profileImageUriFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[profileImageUriKey]
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

    suspend fun saveSession(uid: String) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
            preferences[USER_ID] = uid
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear() // Clears all auth data
        }
    }
}
