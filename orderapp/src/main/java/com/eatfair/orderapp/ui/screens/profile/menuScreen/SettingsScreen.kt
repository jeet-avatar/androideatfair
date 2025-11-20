package com.eatfair.orderapp.ui.screens.profile.menuScreen

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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eatfair.orderapp.model.AppSettings
import com.eatfair.orderapp.ui.screens.profile.ProfileViewModel
import com.eatfair.orderapp.ui.screens.profile.SettingsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToTerms: () -> Unit,
    viewModel: ProfileViewModel
) {
    val settingsState by viewModel.settingsState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
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
        when (val state = settingsState) {
            is SettingsUiState.Success -> {
                SettingsContent(
                    settings = state.settings,
                    onSettingsChange = { viewModel.updateSettings(it) },
                    onNavigateToPrivacyPolicy = onNavigateToPrivacyPolicy,
                    onNavigateToTerms = onNavigateToTerms,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            is SettingsUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF7B3F3F))
                }
            }
            is SettingsUiState.Error -> {
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
fun SettingsContent(
    settings: AppSettings,
    onSettingsChange: (AppSettings) -> Unit,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToTerms: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Manage your preferences and personalize your experience by adjusting options in Settings",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF999999)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Language
        SettingsMenuItem(
            title = "Language(English)",
            onClick = { }
        )

        HorizontalDivider(color = Color(0xFFE0E0E0))

        // Red Card
        SettingsMenuItemWithDetails(
            title = "Red Card",
            detail = "••••3468",
            onClick = { }
        )

        HorizontalDivider(color = Color(0xFFE0E0E0))

        // Allow Notifications
        SettingsMenuItemWithSwitch(
            title = "Allow Notifications",
            checked = settings.notificationsEnabled,
            onCheckedChange = {
                onSettingsChange(settings.copy(notificationsEnabled = it))
            }
        )

        HorizontalDivider(color = Color(0xFFE0E0E0))

        // Sounds
        SettingsMenuItemWithDetails(
            title = "Sounds",
            detail = "Aurora",
            onClick = { }
        )

        HorizontalDivider(color = Color(0xFFE0E0E0))

        // Repeat Alerts
        SettingsMenuItemWithDetails(
            title = "Repeat Alerts",
            detail = "Once",
            onClick = { }
        )

        HorizontalDivider(color = Color(0xFFE0E0E0))

        // Privacy Policy
        SettingsMenuItem(
            title = "Privacy Policy",
            onClick = onNavigateToPrivacyPolicy
        )

        HorizontalDivider(color = Color(0xFFE0E0E0))

        // Terms & Conditions
        SettingsMenuItem(
            title = "Terms & Conditions",
            onClick = onNavigateToTerms
        )
    }
}

@Composable
fun SettingsMenuItem(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF333333)
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Navigate",
            tint = Color(0xFF999999)
        )
    }
}

@Composable
fun SettingsMenuItemWithDetails(
    title: String,
    detail: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF333333)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = detail,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF999999)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = Color(0xFF999999)
            )
        }
    }
}

@Composable
fun SettingsMenuItemWithSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF333333)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF4CAF50),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFE0E0E0)
            )
        )
    }
}