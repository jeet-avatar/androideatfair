package com.eatfair.app.ui.order

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eatfair.app.ui.common.EFTopAppBar
import com.eatfair.app.ui.custom.ShoppingBagIllustration
import com.eatfair.app.ui.theme.pinkVerticalGradient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOrdersScreen(
    onBackClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    onStartOrderingClick: () -> Unit = {},
    selectedTab: CartTab = CartTab.SHOPPING_CARTS
) {
    var currentTab by remember { mutableStateOf(selectedTab) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(pinkVerticalGradient())
    ) {

        EFTopAppBar("My Orders", true, onBackClick, actions = {
            IconButton(onClick = onCartClick) {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = "Cart",
                    tint = Color.Black
                )
            }
        })

        // Tab Row
//        TabSection(
//            selectedTab = currentTab,
//            onTabSelected = { currentTab = it }
//        )

        Spacer(modifier = Modifier.height(40.dp))

        // Empty State Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Shopping Bag Illustration
            ShoppingBagIllustration()

            Spacer(modifier = Modifier.height(40.dp))

            // Title
            Text(
                text = "Your Cart's on a Diet\nWanna Change That?",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text(
                text = "Right now, your cart's looking a little too healthy—completely empty!",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Start Shopping Button
            Button(
                onClick = onStartOrderingClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8B5CF6)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Start Ordering",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

enum class CartTab {
    SHOPPING_CARTS,
    ORDER_AGAIN
}

@Composable
fun TabSection(
    selectedTab: CartTab,
    onTabSelected: (CartTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Shopping carts tab
        TabButton(
            text = "Shopping carts",
            isSelected = selectedTab == CartTab.SHOPPING_CARTS,
            onClick = { onTabSelected(CartTab.SHOPPING_CARTS) },
            modifier = Modifier.weight(1f)
        )

        // Order again tab
        TabButton(
            text = "Order again",
            isSelected = selectedTab == CartTab.ORDER_AGAIN,
            onClick = { onTabSelected(CartTab.ORDER_AGAIN) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF8B5CF6) else Color.Transparent,
            contentColor = if (isSelected) Color.White else Color.Gray
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        )
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
fun ShoppingBagIllustration2() {
    Box(
        modifier = Modifier
            .size(280.dp),
        contentAlignment = Alignment.Center
    ) {
        // You can replace this with an actual image using:
        // Image(
        //     painter = painterResource(id = R.drawable.empty_cart_illustration),
        //     contentDescription = "Empty Cart",
        //     modifier = Modifier.fillMaxSize()
        // )

        // Placeholder illustration
        Canvas(modifier = Modifier.fillMaxSize()) {
        }

        Text(
            text = "🛍️",
            fontSize = 120.sp,
            modifier = Modifier.offset(y = (-20).dp)
        )

        Text(
            text = "EatFair",
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFFD97706),
            style = TextStyle(
                fontStyle = FontStyle.Italic
            ),
            modifier = Modifier.offset(y = 10.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun MyOrdersScreenPreview() {
    MyOrdersScreen()
}