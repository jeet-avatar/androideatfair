package com.eatfair.app.ui.address

//import androidx.privacysandbox.tools.core.generator.build
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eatfair.shared.data.repo.AddressRepo
import com.eatfair.shared.model.address.AddressDto
//import com.google.android.libraries.places.api.Places
//import com.google.android.libraries.places.api.model.AutocompleteSessionToken
//import com.google.android.libraries.places.api.model.Place
//import com.google.android.libraries.places.api.net.FetchPlaceRequest
//import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AutocompleteResult(
    val placeId: String,
    val primaryText: String,
    val secondaryText: String
)

@HiltViewModel
class LocationViewModel @Inject constructor(
    application: Application,
    private val addressRepo: AddressRepo
) : ViewModel() {

//    private val placesClient = Places.createClient(application)
//    private val token = AutocompleteSessionToken.newInstance()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<AutocompleteResult>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    val allAddresses: StateFlow<List<AddressDto>> = addressRepo.getAddresses()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
//        viewModelScope.launch {
//            if (query.length > 2) {
//                try {
//                    val request = FindAutocompletePredictionsRequest.builder()
//                        .setSessionToken(token)
//                        .setQuery(query)
//                        .build()
//
//                    val response = placesClient.findAutocompletePredictions(request).await()
//                    _searchResults.value = response.autocompletePredictions.map {
//                        AutocompleteResult(
//                            placeId = it.placeId,
//                            primaryText = it.getPrimaryText(null).toString(),
//                            secondaryText = it.getSecondaryText(null).toString()
//                        )
//                    }
//                } catch (e: Exception) {
//                    Log.e("LocationViewModel", "Autocomplete failed", e)
//                }
//            } else {
//                _searchResults.value = emptyList()
//            }
//        }
    }

//    suspend fun getCoordinates(placeId: String): LatLng? {
//        val placeFields = listOf(Place.Field.LAT_LNG)
//        val request = FetchPlaceRequest.newInstance(placeId, placeFields)
//        return try {
//            val response = placesClient.fetchPlace(request).await()
//            response.place.latLng
//        } catch (e: Exception) {
//            Log.e("LocationViewModel", "Fetching coordinates failed", e)
//            null
//        }
//    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = emptyList()
    }

    fun saveAddress(addressDto: AddressDto) {
        viewModelScope.launch {
            if (addressDto.id == 0) {
                addressRepo.insertAddress(addressDto)
            } else {
                addressRepo.updateAddress(addressDto)
            }
        }
    }

    fun getAddressById(id: Int): AddressDto? {
        return allAddresses.value.find { it.id == id }
    }

    fun deleteAddress(addressDto: AddressDto) {
        viewModelScope.launch {
            addressRepo.deleteAddress(addressDto)
        }
    }
}
