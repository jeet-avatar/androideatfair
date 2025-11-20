package com.eatfair.orderapp.ui.screens.home

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
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
import com.eatfair.orderapp.R
import com.eatfair.orderapp.constants.OrderStatus
import com.eatfair.orderapp.model.MapPoint
import com.eatfair.orderapp.model.order.DeliveryOrder
import com.eatfair.orderapp.ui.screens.home.components.OrderBottomSheet
import com.eatfair.orderapp.ui.screens.home.utils.EFMapUtils
import com.eatfair.orderapp.ui.screens.home.utils.EFMapUtils.bitmapDescriptorFromVector
import com.eatfair.orderapp.ui.screens.home.utils.EFMapUtils.createCircularImageMarker
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

data class DeliveryStats(
    val todayOrders: Int,
    val todayEarnings: Double,
    val weeklyOrders: Int,
    val weeklyEarnings: Double,
    val rating: Double,
    val completionRate: Double
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val showConfirmDialog by viewModel.showConfirmDialog.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (val state = uiState) {
            is DeliveryUiState.Loading -> {
                LoadingScreen()
            }

            is DeliveryUiState.NoOrders -> {
                NoOrdersScreen()
            }

            is DeliveryUiState.Active -> {
                ActiveDeliveryScreen(
                    order = state.order,
                    onStartNavigation = { viewModel.startNavigation() },
                    onPickupConfirm = { viewModel.confirmPickup() },
                    onDeliveryConfirm = { viewModel.confirmDelivery() },
                    onCallCustomer = { /* Handle call */ },
                    onChatCustomer = { /* Handle chat */ }
                )

                if (showConfirmDialog) {
                    ConfirmDeliveryDialog(
                        order = state.order,
                        onConfirm = { viewModel.confirmDelivery() },
                        onDismiss = { viewModel.hideConfirmDeliveryDialog() }
                    )
                }
            }

            is DeliveryUiState.Error -> {
                ErrorScreen(
                    message = state.message,
                    onRetry = { viewModel.loadActiveOrder() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ActiveDeliveryScreen(
    order: DeliveryOrder,
    onStartNavigation: () -> Unit,
    onPickupConfirm: () -> Unit,
    onDeliveryConfirm: () -> Unit,
    onCallCustomer: () -> Unit,
    onChatCustomer: () -> Unit
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isNavigating by remember { mutableStateOf(false) }
    var currentRoute by remember { mutableStateOf<List<LatLng>>(emptyList()) }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // --- 2. Permissions Handling ---
    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    // --- 3. Location Updates ---
    // This effect runs when permissions are granted to start fetching location
    LaunchedEffect(locationPermissions.allPermissionsGranted) {
        if (locationPermissions.allPermissionsGranted) {
            // This is required for location updates
            @SuppressLint("MissingPermission")
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    currentLocation = LatLng(location.latitude, location.longitude)
                }
            }
        }
    }

    val bottomSheetState =
        rememberStandardBottomSheetState(initialValue = SheetValue.PartiallyExpanded)

    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    val pickupLatLng = LatLng(order.pickupLocation.latitude, order.pickupLocation.longitude)
    val deliveryLatLng = LatLng(order.deliveryLocation.latitude, order.deliveryLocation.longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(pickupLatLng, 15f)
    }

    val startNavigationTo = { destination: LatLng ->
        if (!locationPermissions.allPermissionsGranted) {
            locationPermissions.launchMultiplePermissionRequest()
        } else {
            isNavigating = true
            onStartNavigation() // Update ViewModel state

            // In a real app, you would call Directions API here
            // For now, we simulate a route and animate the camera
            coroutineScope.launch {
                // Simulate fetching a route
                currentRoute = EFMapUtils.getFakeRoute(currentLocation ?: pickupLatLng, destination)

                // Center map on the current location and zoom in for navigation
                currentLocation?.let {
                    cameraPositionState.animate(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition(it, 17f, 0f, 0f)
                        ),
                        durationMs = 1000
                    )
                }
            }
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            // This is the content of your bottom sheet
            Column(Modifier.fillMaxHeight(0.75f)) {
                OrderBottomSheet(
                    order = order,
                    onStartNavigation = {
                        val destination =
                            if (order.status == OrderStatus.PENDING) {
                                pickupLatLng
                            } else deliveryLatLng

                        startNavigationTo(destination)
                        onStartNavigation()
                    },
                    onPickupConfirm = onPickupConfirm,
                    onDeliveryConfirm = onDeliveryConfirm,
                    onCallCustomer = onCallCustomer,
                    onChatCustomer = onChatCustomer
                )
            }
        },
        sheetContainerColor = Color.White,
        sheetPeekHeight = 150.dp, // Adjust this to show a portion of the sheet when collapsed
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Google Map
            val pickupLatLng = LatLng(order.pickupLocation.latitude, order.pickupLocation.longitude)
            val deliveryLatLng =
                LatLng(order.deliveryLocation.latitude, order.deliveryLocation.longitude)
            val midPoint = LatLng(
                (pickupLatLng.latitude + deliveryLatLng.latitude) / 2,
                (pickupLatLng.longitude + deliveryLatLng.longitude) / 2
            )

            val coroutineScope = rememberCoroutineScope()

            val restaurant = MapPoint(
                title = "Monicas Trattoria",
                address = order.pickupLocation.address,
                latLng = pickupLatLng,
                imageRes = R.drawable.ic_restaurant,
                isProfile = false
            )

            val customer = MapPoint(
                title = order.customerName,
                address = order.deliveryLocation.address,
                latLng = deliveryLatLng,
                imageUrl = "https://cdn.pixabay.com/photo/2012/04/18/23/36/boy-38262_1280.png",
                isProfile = true
            )


            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(midPoint, 13f)
            }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = locationPermissions.allPermissionsGranted),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    myLocationButtonEnabled = locationPermissions.allPermissionsGranted,
                    compassEnabled = true,
                )
            )
            {

                @Composable
                @GoogleMapComposable
                fun MapMarkersAndPolyline() {
                    // Restaurant Marker
                    CustomMarker(point = restaurant)

                    // Customer Marker (with profile image)
                    CustomMarker(point = customer)

                    // Polyline between locations
                    Polyline(
                        points = if (currentLocation != null)
                            listOf(currentLocation!!, pickupLatLng, deliveryLatLng)
                        else listOf(pickupLatLng, deliveryLatLng),
                        color = Color(0xFFFFEB3B),
                        width = 10f
                    )

                    if (currentRoute.isNotEmpty()) {
                        Polyline(
                            points = currentRoute,
                            color = Color(0xFF673AB7),
                            width = 15f
                        )
                    }
                }

                MapMarkersAndPolyline()
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .windowInsetsPadding(WindowInsets.statusBars)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back Button
//                    IconButton(
//                        onClick = { },
//                        modifier = Modifier
//                            .background(Color.White, CircleShape)
//                            .size(40.dp)
//                    ) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                            contentDescription = "Back",
//                            tint = Color(0xFF333333)
//                        )
//                    }

                    // Help Button
                    TextButton(
                        onClick = { },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = Color.White
                        ),
                        shape = CircleShape
                    ) {
                        Text(
                            text = "Help",
                            color = Color(0xFF333333)
                        )
                    }
                }

//                Spacer(modifier = Modifier.height(8.dp))
//
//                // Online Toggle
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier
//                        .background(
//                            Color.White, RoundedCornerShape(20.dp)
//                        )
//                ) {
//                    Switch(
//                        modifier = Modifier.scale(0.8f),
//                        checked = true,
//                        onCheckedChange = { },
//                        colors = SwitchDefaults.colors(
//                            checkedThumbColor = Color.White,
//                            checkedTrackColor = Color(0xFF4CAF50)
//                        )
//                    )
//                }
            }

            // Pickup Location Card
            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 80.dp, end = 16.dp)
            ) {
                LocationCard(
                    name = order.pickupLocation.address,
                    address = order.pickupLocation.landmark ?: "",
                    isPickup = true,
                    onClick = {
                        coroutineScope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(pickupLatLng, 15f)
                            )
                        }
                    }
                )
            }

            // Delivery Location Card
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 300.dp, start = 16.dp)
            ) {
                LocationCard(
                    name = order.deliveryLocation.address,
                    address = order.deliveryLocation.landmark ?: "",
                    isPickup = false,
                    onClick = {
                        coroutineScope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(deliveryLatLng, 15f)
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun CustomMarker(point: MapPoint) {
    val context = LocalContext.current
    val markerState = remember { MarkerState(position = point.latLng) }

    val bitmapDescriptor by produceState<BitmapDescriptor?>(initialValue = null, point) {
        value = if (point.isProfile) {
            createCircularImageMarker(context, point)
        } else {
            point.imageRes?.let { bitmapDescriptorFromVector(context, it) }
        }
    }

    bitmapDescriptor?.let {
        Marker(
            state = markerState,
            title = point.title,
            snippet = point.address,
            icon = it
        )
    }
}

@Composable
fun LocationCard(name: String, address: String, isPickup: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFDADADA), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        if (isPickup) Color(0xFFFFE4E1) else Color(0xFFFFE4E1),
//                        RoundedCornerShape(topStart =  8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPickup) Icons.Default.Restaurant else Icons.Default.Home,
                    contentDescription = null,
                    tint = if (isPickup) Color(0xFFFF6B6B) else Color(0xFFFF6B6B),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    maxLines = 1,
                    color = Color(0xFF333333)
                )
                Text(
                    text = address,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 10.sp,
                    color = Color(0xFF999999),
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun ConfirmDeliveryDialog(
    order: DeliveryOrder,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Confirm Delivery",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text("Are you sure you want to complete this delivery?")
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Order: ${order.invoiceId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF666666)
                )
                Text(
                    text = "Customer: ${order.customerName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF666666)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading active orders...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun NoOrdersScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.DeliveryDining,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = Color(0xFF7B3F3F).copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "No Active Deliveries",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "You're all caught up! New orders will appear here.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF666666)
            )
        }
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Something went wrong",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Retry")
            }
        }
    }
}