package com.eatfair.app.ui.address

import android.Manifest
import android.annotation.SuppressLint
import android.location.Address
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import android.location.Geocoder
import android.util.Log
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eatfair.shared.model.address.LocationData
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationMapScreen(
    locationViewModel: LocationViewModel,
    onBackClick: () -> Unit = {},
    onConfirmLocation: (LocationData) -> Unit = {},
) {

    var currentPinLocation by remember {
        mutableStateOf(
            LocationData(
                locationName = "Loading...",
                address = "Fetching your location...",
                latitude = 0.0,
                longitude = 0.0
            )
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(0.0, 0.0),
            15f
        )
    }

    val context = LocalContext.current
    val geocoder = remember { Geocoder(context) }
    val scope = rememberCoroutineScope()

    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    val fusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        scope.launch {
            // Ensure we only proceed if permissions are actually granted
            if (locationPermissionsState.allPermissionsGranted) {
                val lastLocation = fusedLocationProviderClient.lastLocation.await()
                lastLocation?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    cameraPositionState.animate(
                        newLatLngZoom(latLng, cameraPositionState.position.zoom)
                    )
                }
            }
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val isGranted =
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                        permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
            if (isGranted) {
                getCurrentLocation()
            }
        }
    )

    @SuppressLint("MissingPermission")
    fun fetchInitialLocation() {
        if (locationPermissionsState.allPermissionsGranted) {
            getCurrentLocation()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            val newLatLng = cameraPositionState.position.target
            try {
                // The modern API is asynchronous and requires a callback.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(
                        newLatLng.latitude,
                        newLatLng.longitude,
                        1
                    ) { addresses ->
                        if (addresses.isNotEmpty()) {
                            val address = addresses[0]
                            currentPinLocation = currentPinLocation.copy(
                                locationName = address.featureName ?: "Unknown Location",
                                address = address.getAddressLine(0) ?: "Unknown Address"
                            )

                            Log.d("LocationMapScreen", "Address: ${address}")

                        } else {
                            // Handle case where no address is found
                        }
                    }
                } else {
                    // The old API is synchronous and can be wrapped in a coroutine, but it's fine here.
                    @Suppress("DEPRECATION")
                    val addresses =
                        geocoder.getFromLocation(newLatLng.latitude, newLatLng.longitude, 1)
                    if (addresses?.isNotEmpty() == true) {
                        val address = addresses[0]
                        currentPinLocation = currentPinLocation.copy(
                            locationName = address.featureName ?: "Unknown Location",
                            address = address.getAddressLine(0) ?: "Unknown Address"
                        )
                    }
                }
                // Update latitude and longitude outside the conditional address logic
                currentPinLocation = currentPinLocation.copy(
                    latitude = newLatLng.latitude,
                    longitude = newLatLng.longitude,
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        //MapViewPlaceholder()
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLoaded = { fetchInitialLocation() }
        ) {
            // The center pin is a UI overlay, so no Marker is needed here.
        }

        // Top Search Bar
        LocationSearchBar(
            onBackClick = onBackClick,
            onSearchClick = {

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Center Location Pin
        Box(
            modifier = Modifier.align(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            // Blue circle shadow
//            Box(
//                modifier = Modifier
//                    .size(60.dp)
//                    .background(
//                        Color(0xFF2196F3).copy(alpha = 0.3f),
//                        CircleShape
//                    )
//            )
            // Marker for the pin
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Pin", // This icon is orange
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(80.dp)
            )
        }

        // Current Location Button
        Surface(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 170.dp)
                .clip(RoundedCornerShape(24.dp))
                .clickable {
                    // On click, check permissions and then get location
                    if (locationPermissionsState.allPermissionsGranted) {
                        getCurrentLocation()
                    } else {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                },
            color = MaterialTheme.colorScheme.primaryContainer,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Current Location",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Current location",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
        }

        // Bottom Sheet with Location Details
        LocationBottomSheet(
            location = currentPinLocation,
            onConfirmClick = { onConfirmLocation(currentPinLocation) },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun MapViewPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        // Placeholder for Google Maps
        // In real app, use:
        // GoogleMap(...)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Map,
                contentDescription = "Map",
                modifier = Modifier.size(80.dp),
                tint = Color.Gray.copy(alpha = 0.3f)
            )
            Text(
                text = "Map View Placeholder",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "Integrate Google Maps here",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun LocationSearchBar(
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.windowInsetsPadding(WindowInsets.statusBars),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Back Button
        Surface(
            modifier = Modifier.size(48.dp),
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            shadowElevation = 4.dp,
            onClick = onBackClick
        ) {
            Box(
                modifier = Modifier.padding(start = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        }

        // Search Bar
        Surface(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .clickable(onClick = onSearchClick),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Search an area or address",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun LocationBottomSheet(
    location: LocationData,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Order will be delivered here",
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = location.locationName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = location.address,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onConfirmClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Confirm & proceed",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

// ==================== ADDRESS DETAILS SCREEN ====================

// ==================== SAMPLE DATA ====================
//fun getSampleLocation(): LocationData {
//    return LocationData(
//        locationName = "Biratnagar",
//        address = "Biratnagar 56613, Nepal",
//        latitude = 26.4525,
//        longitude = 87.2718
//    )
//}

// ==================== INTEGRATION WITH GOOGLE MAPS ====================
/*
3. Replace MapViewPlaceholder() with:
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun GoogleMapView(location: LocationData) {
    val position = LatLng(location.latitude, location.longitude)
    val cameraPositionState = rememberCameraPositionState {
        this.position = CameraPosition.fromLatLngZoom(position, 15f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        // Add markers, etc.
    }
}
*/