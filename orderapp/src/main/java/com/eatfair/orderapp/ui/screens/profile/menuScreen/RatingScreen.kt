package com.eatfair.orderapp.ui.screens.profile.menuScreen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingScreen(
    onNavigateBack: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var showBottomSheet by remember { mutableStateOf(false) }
    var currentRating by remember { mutableFloatStateOf(4.8f) }
    var totalDeliveries by remember { mutableIntStateOf(847) }
    var fiveStarCount by remember { mutableStateOf(720) }
    var fourStarCount by remember { mutableStateOf(95) }
    var threeStarCount by remember { mutableStateOf(22) }
    var twoStarCount by remember { mutableStateOf(7) }
    var oneStarCount by remember { mutableStateOf(3) }

    val totalRatings = fiveStarCount + fourStarCount + threeStarCount + twoStarCount + oneStarCount

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Rating",
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF333333)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF333333)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFFAFAFA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            // Overall Rating Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showBottomSheet = true },
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Your Overall Rating",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF666666)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Animated Rating Display
                    AnimatedRatingDisplay(rating = currentRating)

                    Spacer(modifier = Modifier.height(12.dp))

                    // Star Icons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = if (index < currentRating.toInt())
                                    Icons.Default.Star
                                else
                                    Icons.Default.StarBorder,
                                contentDescription = null,
                                tint = Color(0xFFFFB800),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "$totalDeliveries Total Deliveries",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF999999)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { showBottomSheet = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7B3F3F)
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("View Detailed Ratings")
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Stats
            Text(
                text = "Quick Stats",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Completion Rate",
                    value = "98.5%",
                    icon = Icons.Default.CheckCircle,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    title = "On-Time",
                    value = "96.2%",
                    icon = Icons.Default.Schedule,
                    color = Color(0xFF2196F3),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Acceptance",
                    value = "92.0%",
                    icon = Icons.Default.ThumbUp,
                    color = Color(0xFFFF9800),
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    title = "Customer Satisfaction",
                    value = "4.8/5.0",
                    icon = Icons.Default.Favorite,
                    color = Color(0xFFE91E63),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    // Rating Bottom Sheet
    if (showBottomSheet) {
        RatingBottomSheet(
            sheetState = sheetState,
            onDismiss = { showBottomSheet = false },
            currentRating = currentRating,
            totalRatings = totalRatings,
            fiveStarCount = fiveStarCount,
            fourStarCount = fourStarCount,
            threeStarCount = threeStarCount,
            twoStarCount = twoStarCount,
            oneStarCount = oneStarCount
        )
    }
}

@Composable
fun AnimatedRatingDisplay(rating: Float) {
    val animatedRating by animateFloatAsState(
        targetValue = rating,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "rating"
    )

    Box(
        contentAlignment = Alignment.Center
    ) {
        // Background Circle
        Canvas(modifier = Modifier.size(120.dp)) {
            drawCircle(
                color = Color(0xFFE0E0E0),
                radius = size.minDimension / 2
            )
        }

        // Animated Progress Circle
        Canvas(modifier = Modifier.size(120.dp)) {
            val sweepAngle = (animatedRating / 5f) * 360f
            drawArc(
                color = Color(0xFF7B3F3F),
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 12f)
            )
        }

        // Rating Text
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = String.format(Locale.US,"%.1f", animatedRating),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                fontSize = 36.sp
            )
            Text(
                text = "out of 5",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF999999),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF999999),
                fontSize = 12.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    currentRating: Float,
    totalRatings: Int,
    fiveStarCount: Int,
    fourStarCount: Int,
    threeStarCount: Int,
    twoStarCount: Int,
    oneStarCount: Int
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Handle Bar
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFFE0E0E0))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Rating Breakdown",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Based on $totalRatings ratings",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF999999)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Overall Rating Summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = String.format(Locale.US,"%.1f", currentRating),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    Row {
                        repeat(5) { index ->
                            Icon(
                                imageVector = if (index < currentRating.toInt())
                                    Icons.Default.Star
                                else
                                    Icons.Default.StarBorder,
                                contentDescription = null,
                                tint = Color(0xFFFFB800),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Rating Bars
            AnimatedRatingBar(5, fiveStarCount, totalRatings)
            Spacer(modifier = Modifier.height(12.dp))
            AnimatedRatingBar(4, fourStarCount, totalRatings)
            Spacer(modifier = Modifier.height(12.dp))
            AnimatedRatingBar(3, threeStarCount, totalRatings)
            Spacer(modifier = Modifier.height(12.dp))
            AnimatedRatingBar(2, twoStarCount, totalRatings)
            Spacer(modifier = Modifier.height(12.dp))
            AnimatedRatingBar(1, oneStarCount, totalRatings)

            Spacer(modifier = Modifier.height(24.dp))

            // Recent Reviews Section
            Text(
                text = "Recent Customer Feedback",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(12.dp))

            ReviewItem(
                customerName = "Sarah M.",
                rating = 5,
                comment = "Very professional and delivered right on time. Food was still hot!",
                date = "2 days ago"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ReviewItem(
                customerName = "John D.",
                rating = 5,
                comment = "Great communication and careful handling of the order.",
                date = "1 week ago"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ReviewItem(
                customerName = "Emily R.",
                rating = 4,
                comment = "Good service overall. Would order again!",
                date = "2 weeks ago"
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun AnimatedRatingBar(
    stars: Int,
    count: Int,
    total: Int
) {
    val percentage = if (total > 0) (count.toFloat() / total.toFloat()) else 0f
    val animatedPercentage by animateFloatAsState(
        targetValue = percentage,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "percentage"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.width(60.dp)
        ) {
            Text(
                text = "$stars",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF666666),
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFFFB800),
                modifier = Modifier.size(18.dp)
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFFE0E0E0))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedPercentage)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        when (stars) {
                            5 -> Color(0xFF4CAF50)
                            4 -> Color(0xFF8BC34A)
                            3 -> Color(0xFFFFB800)
                            2 -> Color(0xFFFF9800)
                            else -> Color(0xFFFF5252)
                        }
                    )
            )
        }

        Text(
            text = count.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF666666),
            modifier = Modifier.width(40.dp)
        )
    }
}

@Composable
fun ReviewItem(
    customerName: String,
    rating: Int,
    comment: String,
    date: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F8F8)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF7B3F3F)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = customerName.first().toString(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = customerName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF333333)
                        )
                        Row {
                            repeat(rating) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFB800),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF999999),
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = comment,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF666666),
                lineHeight = 20.sp
            )
        }
    }
}