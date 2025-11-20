package com.eatfair.orderapp.ui.screens.profile.menuScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.eatfair.orderapp.model.profile.UserProfile
import com.eatfair.orderapp.ui.screens.profile.ProfileUiState
import com.eatfair.orderapp.ui.screens.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel
) {
    val profileState by viewModel.profileState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Personal Information",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        when (val state = profileState) {
            is ProfileUiState.Success -> {
                PersonalInfoContent(
                    profile = state.profile,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            is ProfileUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF7B3F3F))
                }
            }

            is ProfileUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(state.message, color = Color(0xFFFF5252))
                }
            }
        }
    }
}

@Composable
fun PersonalInfoContent(
    profile: UserProfile,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()) // Makes the content scrollable
    ) {
        // Profile Image
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFF7B3F3F))
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = profile.username.firstOrNull()?.toString() ?: "M",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Username
        InfoField(
            label = "Username",
            value = profile.username,
            icon = Icons.Default.Person
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Email
        InfoField(
            label = "Email",
            value = profile.email,
            icon = Icons.Default.Email
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Password
        InfoField(
            label = "Password",
            value = profile.password,
            icon = Icons.Default.Lock,
            isPassword = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        // SSN (for US)
        InfoField(
            label = "Social Security Number",
            value = profile.socialSecurityNumber ?: "XXX-XX-XXXX",
            icon = Icons.Default.Badge
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Document Card (Driver's License)
        if (profile.driverLicenseNumber != null) {

            Text(
                text = "Driver's License",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Start)
            )

            AsyncImage(
                model = profile.driverLicenseImageUrl,
                contentDescription = "Driver's License",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )

            Text(
                text = "Front",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF666666),
                modifier = Modifier.align(Alignment.End)
            )

            Text(
                text = "Delete",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFFF5252),
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { }
            )
        }
    }
}

@Composable
fun InfoField(
    label: String,
    value: String,
    icon: ImageVector,
    isPassword: Boolean = false
) {

    var passwordVisible by remember { mutableStateOf(!isPassword) }

    OutlinedTextField(
        value = value,
        onValueChange = {}, // Value is read-only, so onValueChange is empty
        modifier = Modifier.fillMaxWidth(),
        readOnly = true, // Makes the field non-editable
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color(0xFF999999)
            )
        },
        trailingIcon = {
            if (isPassword) {
                val image =
                    if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            }
        },
        visualTransformation = if (isPassword && !passwordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFF5F5F5),
            focusedContainerColor = Color(0xFFF5F5F5),
            disabledIndicatorColor = Color.Transparent, // Hides underline when readOnly
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary // Shows border color on focus
        )
    )

//    Column {
//        Text(
//            text = label,
//            style = MaterialTheme.typography.bodyMedium,
//            color = Color(0xFF999999),
//            fontSize = 14.sp
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(
//                imageVector = icon,
//                contentDescription = label,
//                tint = Color(0xFF999999),
//                modifier = Modifier.size(20.dp)
//            )
//            Spacer(modifier = Modifier.width(12.dp))
//            Text(
//                text = value,
//                style = MaterialTheme.typography.bodyLarge,
//                color = Color(0xFF333333)
//            )
//            if (isPassword) {
//                Spacer(modifier = Modifier.weight(1f))
//                Icon(
//                    imageVector = Icons.Default.Visibility,
//                    contentDescription = "Toggle visibility",
//                    tint = Color(0xFF999999),
//                    modifier = Modifier.size(20.dp)
//                )
//            }
//        }
//    }
}