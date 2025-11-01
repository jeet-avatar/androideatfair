package com.eatfair.app.ui.profile

//import coil3.compose.AsyncImage
import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.eatfair.app.R
import com.eatfair.app.model.profile.ProfileMenuItemDto
import com.eatfair.app.ui.common.EFTopAppBar
import com.eatfair.app.ui.theme.primaryVerticalGradient
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    userName: String = "Manish Agrawal",
    userEmail: String = "manish.agrawal.np@gmail.com",
    onBackClick: () -> Unit = {},
    onSavedAddressClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    onPaymentMethodClick: () -> Unit = {},
    onOrderHistoryClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onReferAndEarnClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {

    val profileImageUri by profileViewModel.profileImageUri.collectAsState()

    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    // Launcher for picking from gallery
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> profileViewModel.onProfileImageChange(uri) }
    )

    // Launcher for taking a picture
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                profileViewModel.onProfileImageChange(tempCameraUri)
            }
        }
    )

    // Permission state for Camera
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    fun getTempUri(): Uri {
        val file = File.createTempFile("profile_image_", ".jpg", context.cacheDir)
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryVerticalGradient())
    ) {

        EFTopAppBar("Profile", true, onBackClick)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Profile Image and Info
            ProfileHeader(
                userName = userName,
                userEmail = userEmail,
                profileImageUri = profileImageUri,
//                onChangePhotoClick = {
//                    photoPickerLauncher.launch(
//                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//                    )
//                }
                onGalleryClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onCameraClick = {
                    if (cameraPermissionState.status.isGranted) {
                        tempCameraUri = getTempUri()
                        cameraLauncher.launch(tempCameraUri!!)
                    } else {
                        cameraPermissionState.launchPermissionRequest()
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Setting Title
            Text(
                text = "Setting",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Menu Items
            val menuItems = listOf(
                ProfileMenuItemDto(
                    icon = Icons.Outlined.Person,
                    title = "Edit Profile",
                    onClick = onEditProfileClick
                ),
                ProfileMenuItemDto(
                    icon = Icons.Outlined.Person,
                    title = "Saved Address",
                    onClick = onSavedAddressClick
                ),
//                ProfileMenuItemDto(
//                    icon = Icons.Filled.CreditCard,
//                    title = "Payment Method",
//                    onClick = onPaymentMethodClick
//                ),
//                ProfileMenuItemDto(
//                    icon = Icons.Filled.Language,
//                    title = "Language",
//                    onClick = onLanguageClick
//                ),
                ProfileMenuItemDto(
                    icon = Icons.Filled.History,
                    title = "Order History",
                    onClick = onOrderHistoryClick
                ),
                ProfileMenuItemDto(
                    icon = Icons.Outlined.Notifications,
                    title = "Notifications",
                    onClick = onNotificationsClick
                ),
                ProfileMenuItemDto(
                    icon = Icons.Outlined.PersonAdd,
                    title = "Invite Friend",
                    onClick = onReferAndEarnClick
                ),
                ProfileMenuItemDto(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    title = "Logout",
                    onClick = onLogoutClick
                )
            )

            menuItems.forEach { item ->
                SettingMenuItem(
                    icon = item.icon,
                    title = item.title,
                    onClick = item.onClick
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun ProfileHeader(
    userName: String,
    userEmail: String,
    profileImageUri: Uri?,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
//    onChangePhotoClick: () -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        ImageSourceDialog(
            onDismissRequest = { showDialog = false },
            onCameraClick = onCameraClick,
            onGalleryClick = onGalleryClick
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Image with Camera Button
        Box(
            contentAlignment = Alignment.BottomEnd
        ) {
            // Profile Image
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (profileImageUri != null) {
                    AsyncImage(
                        model = profileImageUri,
                        contentDescription = "Profile Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        error = painterResource(id = R.drawable.ic_person_placeholder),
                        placeholder = painterResource(id = R.drawable.ic_person_placeholder)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Profile",
                        modifier = Modifier.size(60.dp),
                        tint = Color.White
                    )
                }
            }

            // Camera Button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.dp, Color(0xFFFFF5F7), CircleShape)
                    .clickable(onClick = { showDialog = true }),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.CameraAlt,
                    contentDescription = "Change Photo",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User Name
        Text(
            text = userName,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(4.dp))

        // User Email
        Text(
            text = userEmail,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun ImageSourceDialog(
    onDismissRequest: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Change Profile Photo") },
        text = { Text("Choose a source for your new profile picture.") },
        confirmButton = {
            TextButton(onClick = {
                onCameraClick()
                onDismissRequest()
            }) {
                Text("Camera")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onGalleryClick()
                onDismissRequest()
            }) {
                Text("Gallery")
            }
        }
    )
}


@Composable
fun SettingMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(24.dp),
                    tint = Color.Gray
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = title,
                    fontSize = 15.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Navigate",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// Alternative version with light shadow effect
@Composable
fun SettingMenuItemWithShadow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier.size(20.dp),
                        tint = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = title,
                    fontSize = 15.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Navigate",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}