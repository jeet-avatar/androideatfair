package com.eatfair.orderapp.ui.screens.profile.menuScreen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Privacy Policy",
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
        androidx.compose.foundation.lazy.LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            item {
                Text(
                    text = "Effective Date: January 1, 2025",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF999999)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "This Privacy Policy explains how the personal information of delivery partners is collected, used, and protected. We are committed to protecting your data such as contact details, location tracking, payment information, and delivery history, ensuring that all sensitive information is handled securely and only used as necessary for third-party integrations.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF333333),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Data Security",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "We implement industry-standard security measures to protect your personal information. All data is encrypted during transmission and storage. Access to your information is restricted to authorized personnel only. We regularly audit our security practices to ensure compliance with data protection laws including CCPA (California Consumer Privacy Act) and other applicable US regulations.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF666666),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Your Rights",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "You have the right to:\n" +
                            "• Access your personal information\n" +
                            "• Request correction of inaccurate data\n" +
                            "• Request deletion of your data (subject to legal requirements)\n" +
                            "• Opt-out of non-essential data collection\n" +
                            "• Receive a copy of your data in a portable format",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF666666),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Third-Party Sharing",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "We may share your information with:\n" +
                            "• Restaurants and customers (limited to delivery-related information)\n" +
                            "• Payment processors for earnings distribution\n" +
                            "• Insurance providers for coverage purposes\n" +
                            "• Law enforcement when required by law\n\n" +
                            "We do not sell your personal information to third parties.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF666666),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Contact Us",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "If you have questions about this Privacy Policy or wish to exercise your rights, contact us at:\n\n" +
                            "Email: privacy@deliverypartner.com\n" +
                            "Phone: 1-800-DELIVERY\n" +
                            "Address: 123 Delivery Lane, San Francisco, CA 94102",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF666666),
                    lineHeight = 22.sp
                )
            }
        }
    }
}