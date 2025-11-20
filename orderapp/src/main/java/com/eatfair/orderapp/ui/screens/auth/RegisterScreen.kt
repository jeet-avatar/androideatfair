package com.eatfair.orderapp.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eatfair.orderapp.constants.VehicleType
import com.eatfair.orderapp.model.auth.Requirement

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var passwordVisible by remember { mutableStateOf(false) }
    var isVehicleDropdownExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(24.dp)
            ) {
                Text(
                    text = "Drive & Earn Fairly",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Keep 100% of your earnings. No hidden fees. Join your local delivery community.",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .padding(bottom = 80.dp)
            ) {

                // Driver Information Section
                AnimatedSection(delay = 100) {
                    Text(
                        text = "Driver Information",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }

                // Full Name
                AnimatedSection(delay = 200) {
                    OutlinedTextField(
                        value = uiState.registration.fullName,
                        onValueChange = { viewModel.updateFullName(it) },
                        label = { Text("Full Name") },
                        placeholder = { Text("Enter your full name") },
                        isError = uiState.validationErrors.containsKey("fullName"),
                        supportingText = uiState.validationErrors["fullName"]?.let { { Text(it) } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                // Phone Number
                AnimatedSection(delay = 300) {
                    OutlinedTextField(
                        value = uiState.registration.phoneNumber,
                        onValueChange = { viewModel.updatePhoneNumber(it) },
                        label = { Text("Phone Number") },
                        placeholder = { Text("(555) 123-4567") },
                        isError = uiState.validationErrors.containsKey("phoneNumber"),
                        supportingText = uiState.validationErrors["phoneNumber"]?.let { { Text(it) } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone
                        ),
                    )
                }

                // Email Address
                AnimatedSection(delay = 400) {
                    OutlinedTextField(
                        value = uiState.registration.email,
                        onValueChange = { viewModel.updateEmail(it) },
                        label = { Text("Email Address") },
                        placeholder = { Text("your.email@example.com") },
                        isError = uiState.validationErrors.containsKey("email"),
                        supportingText = uiState.validationErrors["email"]?.let { { Text(it) } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                    )
                }

                // Password
                AnimatedSection(delay = 500) {
                    OutlinedTextField(
                        value = uiState.registration.password,
                        onValueChange = { viewModel.updatePassword(it) },
                        label = { Text("Password") },
                        placeholder = { Text("Create a password") },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                                )
                            }
                        },
                        isError = uiState.validationErrors.containsKey("password"),
                        supportingText = uiState.validationErrors["password"]?.let { { Text(it) } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                // Zip Code
                AnimatedSection(delay = 600) {
                    Column {
                        OutlinedTextField(
                            value = uiState.registration.zipCode,
                            onValueChange = { viewModel.updateZipCode(it) },
                            label = { Text("Zip Code") },
                            placeholder = { Text("90210") },
                            isError = uiState.validationErrors.containsKey("zipCode"),
                            supportingText = uiState.validationErrors["zipCode"]?.let { { Text(it) } },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                        )
                        Text(
                            text = "We limit to 5 drivers per zip code for fair earning opportunities",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 16.dp)
                        )
                    }
                }

                // Vehicle Type
                AnimatedSection(delay = 700) {
                    ExposedDropdownMenuBox(
                        expanded = isVehicleDropdownExpanded,
                        onExpandedChange = { isVehicleDropdownExpanded = it },
                        modifier = Modifier.padding(bottom = 32.dp)
                    ) {
                        val fillMaxWidth = Modifier
                            .fillMaxWidth()
                        OutlinedTextField(
                            value = uiState.registration.vehicleType?.displayName ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Vehicle Type") },
                            placeholder = { Text("Select your vehicle type") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isVehicleDropdownExpanded) },
                            isError = uiState.validationErrors.containsKey("vehicleType"),
                            supportingText = uiState.validationErrors["vehicleType"]?.let {
                                {
                                    Text(
                                        it
                                    )
                                }
                            },
                            modifier = fillMaxWidth.menuAnchor(
                                ExposedDropdownMenuAnchorType.PrimaryEditable,
                                true
                            ),
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                            shape = RoundedCornerShape(8.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = isVehicleDropdownExpanded,
                            onDismissRequest = { isVehicleDropdownExpanded = false }
                        ) {
                            VehicleType.entries.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.displayName) },
                                    onClick = {
                                        viewModel.updateVehicleType(type)
                                        isVehicleDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Requirements Section
                AnimatedSection(delay = 800) {
                    Text(
                        text = "Requirements",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                uiState.requirements.forEachIndexed { index, requirement ->
                    AnimatedSection(delay = 900 + (index * 100)) {
                        RequirementItem(requirement = requirement)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Earnings Potential Section
                AnimatedSection(delay = 1300) {
                    EarningsPotentialCard()
                }

                // Login Link
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account? ",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    TextButton(
                        onClick = onLoginClick,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Log In",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        // Register Button
        AnimatedSection(
            delay = 1400,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
        ) {
            Button(
                onClick = { viewModel.registerDriver() },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "Register as Driver - FREE",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // Success Dialog
    if (uiState.isRegistrationSuccess) {
        SuccessDialog(onRegisterClick)
    }

    // Error Snackbar
    uiState.errorMessage?.let { error ->
        LaunchedEffect(error) {
            // Show snackbar
        }
    }
}

@Composable
fun AnimatedSection(
    delay: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delay.toLong())
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(500)) +
                slideInVertically(
                    animationSpec = tween(500),
                    initialOffsetY = { it / 4 }
                ),
        modifier = modifier
    ) {
        content()
    }
}

@Composable
fun RequirementItem(requirement: Requirement) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            modifier = Modifier.size(24.dp),
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = requirement.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = requirement.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun EarningsPotentialCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Earnings Potential",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            EarningRow("Average per delivery:", "$8-15")
            EarningRow("Peak hour bonus:", "+$3-5")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Keep 100% of:",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Tips + Fees",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00A67E)
                )
            }
        }
    }
}

@Composable
fun EarningRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SuccessDialog(
    onRegisterClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    LaunchedEffect(Unit) {
        visible = true
    }

    AlertDialog(
        onDismissRequest = { },
        icon = {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF00A67E),
                modifier = Modifier
                    .size(64.dp)
                    .scale(scale)
            )
        },
        title = {
            Text(
                text = "Registration Successful!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text("Your driver registration has been submitted. We'll review your application and get back to you soon.")
        },
        confirmButton = {
            TextButton(onClick = {
                onRegisterClick()
            }) {
                Text("Got it")
            }
        }
    )
}