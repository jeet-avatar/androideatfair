package com.eatfair.app.ui.home

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.eatfair.app.R
import com.eatfair.app.model.home.CarouselItem
import com.eatfair.app.model.home.Category
import com.eatfair.app.model.home.FoodItem
import com.eatfair.app.model.restaurant.Restaurant
import com.eatfair.app.ui.cart.CartSnackBar
import com.eatfair.app.ui.cart.CartViewModel
import com.eatfair.app.ui.order.TrackOrderSnackBar
import com.eatfair.app.ui.restaurant.RestaurantCard
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    cartViewModel: CartViewModel,
    onProfileClick: () -> Unit = {},
    onLocationClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onCategoryClick: (Category) -> Unit = {},
    onFoodItemClick: (FoodItem) -> Unit = {},
    onRestaurantClick: (Restaurant) -> Unit = {},
    onViewCartClick: () -> Unit = {},
    onTrackOrderClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val restaurants = homeViewModel.restaurants.collectAsState()
    val carouselItems = homeViewModel.carouselItems.collectAsState()
    val categories = homeViewModel.categories.collectAsState()
    val topRatedItems = homeViewModel.topRatedFood.collectAsState()
    val profileImageUri by homeViewModel.profileImageUri.collectAsState()
    val defaultAddress by homeViewModel.defaultAddress.collectAsState()

    val cartItems by cartViewModel.cartItems.collectAsState()
    val activeOrder by cartViewModel.activeOrder.collectAsState()

    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            val order = activeOrder

            if (order != null) {
                TrackOrderSnackBar(
                    order = order,
                    onClick = { onTrackOrderClick(order.orderId) }
                )
            } else if (cartItems.isNotEmpty()) {
                CartSnackBar(
                    cartItems,
                    onClick = onViewCartClick
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
//                .padding(innerPadding)
        ) {
            item {

                Box(
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.inversePrimary, // Start Color (e.g., Deep Purple)
                                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)  // End Color (e.g., Lighter Purple/Pink)
                                )
                                // startY = 0.0f,
                                // endY = Float.POSITIVE_INFINITY
                            )
                        )
                ) {
                    Column {

                        Spacer(modifier = Modifier.height(30.dp))
                        // Top Bar
                        TopBar(
                            onProfileClick = onProfileClick,
                            onLocationClick = onLocationClick,
                            profileImageUri = profileImageUri,
                            addressLine1 = defaultAddress?.type?.displayName + "\n" + defaultAddress?.addressLine2()
                        )

                        // Search Bar
                        SearchBar(
                            searchQuery = searchQuery,
                            onSearchQueryChange = { searchQuery = it },
                            onSearchClick = onSearchClick
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Carousel
                        CarouselSection(items = carouselItems.value)

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

//        Spacer(modifier = Modifier.height(12.dp))

            // Categories Section
            item {
                CategoriesSection(
                    categories = categories.value,
                    onCategoryClick = onCategoryClick,
                )
            }

            // Top Rated Section
            item {
                Spacer(modifier = Modifier.height(24.dp))

                TopRatedSection(
                    items = topRatedItems.value,
                    onItemClick = onFoodItemClick
                )
            }

            item {
                Text(
                    text = "All Restaurants",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
                )
            }

            // The Restaurant List
            items(restaurants.value) { restaurant ->
                RestaurantCard(
                    restaurant = restaurant,
                    onClick = {
                        onRestaurantClick(restaurant)
                    },
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ) // Add padding here
                )
            }

            // Bottom Navigation Bar if required
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }

}

@Composable
fun TopBar(
    onProfileClick: () -> Unit,
    onLocationClick: () -> Unit,
    profileImageUri: Uri?,
    addressLine1: String? = null
) {

    var isSheetOpen by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val locationTxt = addressLine1?.take(22).plus("..")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Location
        Row(
            modifier = Modifier
                .height(40.dp)
                .clip(RoundedCornerShape(50))
                .clickable(onClick = {
//                    isSheetOpen = true
                    onLocationClick()
                }),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = "Location",
                modifier = Modifier.size(20.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = locationTxt ?: "Locating...",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                lineHeight = TextUnit(18f, TextUnitType.Sp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Change Location",
                modifier = Modifier.size(20.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        // Profile Icon
        IconButton(
            onClick = onProfileClick,
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.inversePrimary)
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
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Profile",
                    tint = Color.Black
                )
            }
        }
    }

    LocationBottomSheet(
        isOpen = isSheetOpen,
        onDismissRequest = { isSheetOpen = false },
        onItemClick = {
            // Handle item clicks and close the sheet
            isSheetOpen = false
            // You can add navigation/logic based on the item clicked here
        }
    )
}

@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Release) {
                onSearchClick() // Call the navigation callback
            }
        }
    }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        singleLine = true,
        maxLines = 1,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        placeholder = {
            Text(
                text = "Search for restaurants, food and more...",
                color = Color.Gray,
                fontSize = 14.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF5F5F5),
            unfocusedContainerColor = Color(0xFFF5F5F5),
            disabledContainerColor = Color(0xFFF5F5F5),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(12.dp),
        readOnly = true,
        interactionSource = interactionSource
    )
}

@Composable
fun CarouselSection(items: List<CarouselItem>) {
    val pagerState = rememberPagerState(pageCount = { items.size })
    val pagerIsDragged by pagerState.interactionSource.collectIsDraggedAsState()

    val pageInteractionSource = remember { MutableInteractionSource() }
    val pageIsPressed by pageInteractionSource.collectIsPressedAsState()

    // Stop auto-advancing when pager is dragged or one of the pages is pressed
    val autoAdvance = !pagerIsDragged && !pageIsPressed

    if (autoAdvance) {
        LaunchedEffect(pagerState, pageInteractionSource) {
            while (true) {
                delay(3000)
                val nextPage = (pagerState.currentPage + 1) % items.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 14.dp),
            modifier = Modifier.height(140.dp),
        ) { page ->
            CarouselCard(item = items[page])
        }

        Spacer(modifier = Modifier.height(12.dp))

        PagerIndicator(items.size, pagerState.currentPage)
    }
}

@Composable
fun PagerIndicator(pageCount: Int, currentPageIndex: Int, modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { iteration ->
                val color =
                    if (currentPageIndex == iteration) MaterialTheme.colorScheme.primaryContainer else Color.LightGray
                Box(
                    modifier = modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(height = 4.dp, width = 12.dp)
                )
            }
        }
    }
}

@Composable
fun CarouselCard(item: CarouselItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = item.backgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.title,
                    fontSize = 10.sp,
                    color = Color.Black.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.subtitle,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description,
                    fontSize = 11.sp,
                    color = Color.Black.copy(alpha = 0.8f),
                    lineHeight = 14.sp
                )
            }

            // Placeholder for image
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "Carousel Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
        }
    }
}

@Composable
fun CategoriesSection(
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit,
) {

    var selectedCategoryId by remember { mutableStateOf(categories.first().id) }

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Categories",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->

                val isSelected = category.id == selectedCategoryId

                CategoryItem(
                    category = category,
                    isSelected = isSelected,
                    onClick = {
                        selectedCategoryId = category.id
                        onCategoryClick(category)
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.tertiary else Color.Transparent,
            )
            .border(1.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(50))
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .padding(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category.emoji,
                fontSize = 32.sp
            )
        }

//        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = category.name,
            fontSize = 12.sp,
            color = if (isSelected) Color.White else Color.DarkGray,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun TopRatedSection(
    items: List<FoodItem>,
    onItemClick: (FoodItem) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Top-Rated",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            TextButton(onClick = { /* See all action */ }) {
                Text(
                    text = "See all",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Grid of items (2 columns)
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { item ->
                        FoodItemCard(
                            item = item,
                            onClick = { onItemClick(item) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Add empty space if odd number of items
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun FoodItemCard(
    item: FoodItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
//        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE5D4FF)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🍖",
                    fontSize = 64.sp
                )
            }

            // Price and rating
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.9f))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = item.name,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$${item.price}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                // Add button
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .clickable(onClick = onClick),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar({}, {}, null)

}