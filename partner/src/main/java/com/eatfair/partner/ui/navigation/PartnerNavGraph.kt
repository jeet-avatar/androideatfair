package com.eatfair.partner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eatfair.partner.ui.home.PartnerHomeScreen
import com.eatfair.partner.ui.orders.OrdersScreen
import com.eatfair.partner.ui.menu.MenuScreen
import com.eatfair.partner.ui.notifications.NotificationsScreen
import com.eatfair.partner.ui.profile.ProfileScreen

sealed class PartnerScreen(val route: String) {
    data object Home : PartnerScreen("home")
    data object Orders : PartnerScreen("orders")
    data object Menu : PartnerScreen("menu")
    data object Notifications : PartnerScreen("notifications")
    data object Profile : PartnerScreen("profile")
}

@Composable
fun PartnerNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = PartnerScreen.Home.route,
        modifier = modifier
    ) {
        composable(PartnerScreen.Home.route) {
            PartnerHomeScreen(
                onNavigateToOrders = { navController.navigate(PartnerScreen.Orders.route) },
                onNavigateToMenu = { navController.navigate(PartnerScreen.Menu.route) },
                onNavigateToNotifications = { navController.navigate(PartnerScreen.Notifications.route) },
                onNavigateToProfile = { navController.navigate(PartnerScreen.Profile.route) }
            )
        }
        
        composable(PartnerScreen.Orders.route) {
            OrdersScreen(
                onBackClick = { navController.navigateUp() },
                onOrderClick = { orderId ->
                    // TODO: Navigate to order details
                }
            )
        }
        
        composable(PartnerScreen.Menu.route) {
            MenuScreen(
                onBackClick = { navController.navigateUp() }
            )
        }
        
        composable(PartnerScreen.Notifications.route) {
            NotificationsScreen(
                onBackClick = { navController.navigateUp() }
            )
        }
        
        composable(PartnerScreen.Profile.route) {
            ProfileScreen(
                onBackClick = { navController.navigateUp() },
                onLogout = {
                    // TODO: Handle logout
                    navController.navigate(PartnerScreen.Home.route) {
                        popUpTo(PartnerScreen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
