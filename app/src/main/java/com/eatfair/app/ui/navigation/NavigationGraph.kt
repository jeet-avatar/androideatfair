package com.eatfair.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.eatfair.app.model.address.LocationData
import com.eatfair.app.ui.address.AddAddressDetailsScreen
import com.eatfair.app.ui.address.LocationMapScreen
import com.eatfair.app.ui.address.LocationViewModel
import com.eatfair.app.ui.address.SavedAddressesScreen
import com.eatfair.app.ui.auth.AuthState
import com.eatfair.app.ui.auth.AuthViewModel
import com.eatfair.app.ui.auth.LoginScreen
import com.eatfair.app.ui.auth.RegisterScreen
import com.eatfair.app.ui.auth.WelcomeScreen
import com.eatfair.app.ui.cart.CartScreen
import com.eatfair.app.ui.cart.CartViewModel
import com.eatfair.app.ui.home.HomeScreen
import com.eatfair.app.ui.home.HomeViewModel
import com.eatfair.app.ui.notification.NotificationScreen
import com.eatfair.app.ui.notification.getSampleNotifications
import com.eatfair.app.ui.order.MyOrdersScreen
import com.eatfair.app.ui.order.OrderTrackingScreen
import com.eatfair.app.ui.profile.EditProfileScreen
import com.eatfair.app.ui.profile.ProfileScreen
import com.eatfair.app.ui.profile.ProfileViewModel
import com.eatfair.app.ui.refer.ReferAndEarnScreen
import com.eatfair.app.ui.restaurant.RestaurantListScreen
import com.eatfair.app.ui.restaurant.RestaurantScreen
import com.eatfair.app.ui.restaurant.RestaurantViewModel
import com.eatfair.app.ui.search.SearchScreen
import com.eatfair.app.ui.search.SearchViewModel

@Composable
fun NavigationGraph(
//    innerPadding: PaddingValues,
    startDestination: String = "auth_flow"
) {

    val authViewModel: AuthViewModel = hiltViewModel()
    val authState = authViewModel.authState.collectAsState().value

    val cartViewModel: CartViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()

    val navController = rememberNavController()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Initial, AuthState.Loading -> {
                // Stay on splash/loading screen while the session check is ongoing
//                navController.navigate(Screen.Splash.route) {
//                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
//                }
            }

            is AuthState.Authenticated -> {
                // User logged in: Redirect to Home, clearing the back stack
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Welcome.route) { inclusive = true }
                    launchSingleTop = true
                }
            }

            AuthState.UnAuthenticated, is AuthState.Error -> {
                // User logged out or failed login: Redirect to Login, clearing back stack
                navController.navigate(Screen.Welcome.route) {
                    popUpTo(Screen.Welcome.route) { inclusive = false }
                    launchSingleTop = true
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        authGraph(navController, authViewModel)

        composable(route = Screen.Home.route) {
            HomeScreen(
                homeViewModel = homeViewModel,
                cartViewModel = cartViewModel,
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                },
                onLocationClick = {
                    navController.navigate(Screen.LocationMap.route)
                },
                onSearchClick = { navController.navigate(Screen.Search.route) },
                onCategoryClick = {},
                onFoodItemClick = {},
                onRestaurantClick = {
                    navController.navigate(Screen.RestaurantFlow.createRoute(it.id.toString()))
                },
                onViewCartClick = {
                    navController.navigate(Screen.Cart.route)
                },
                onTrackOrderClick = { orderId ->
                    navController.navigate(Screen.OrderTrackingScreen.createRoute(orderId))
                }
            )
        }

        composable(route = Screen.Search.route) {
            val searchViewModel: SearchViewModel = viewModel()

            SearchScreen(
                searchViewModel = searchViewModel,
                onBackClick = { navController.navigateUp() },
                onRecentSearchClick = { search ->
                    searchViewModel.searchByTerm(search)
                },
                onCuisineClick = { cuisine ->
                    searchViewModel.searchByTerm(cuisine)
                },
                onSearchResultClick = { result ->
                    navController.navigate(Screen.RestaurantFlow.createRoute(result.id.toString()))
                }
            )
        }

        composable(route = Screen.Cart.route) { backStackEntry ->

            val selectedRestaurant by cartViewModel.cartRestaurant.collectAsState()

            selectedRestaurant?.let { restaurant ->
                CartScreen(
                    cartViewModel = cartViewModel,
                    onBackClick = { navController.navigateUp() },
                    onAddMoreItems = {
                        //check if can go back to restaurant screen
                        navController.navigate(Screen.Restaurant.route) {
                            popUpTo(Screen.Restaurant.route) {
                                inclusive = true
                            }
                        }

                    },
                    onPlaceOrderSuccess = { newOrder ->
                        navController.navigate(Screen.OrderTrackingScreen.createRoute(newOrder.orderId)) {
                            popUpTo(Screen.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                )
            }
        }

        composable(Screen.OrderTrackingScreen.route) { backStackEntry ->

            val orderId = backStackEntry.arguments?.getString("orderId")

            OrderTrackingScreen(
                cartViewModel = cartViewModel,
                onBackClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                    }
                },
                onCallPartner = {
                    // Make phone call
                },
                onAddInstructions = {
                    // Show instruction dialog
                },
            )
        }

        addressGraph(navController)

        restaurantGraph(navController, cartViewModel)

        profileGraph(navController, authViewModel, profileViewModel)
    }
}

fun NavGraphBuilder.authGraph(navController: NavHostController, authViewModel: AuthViewModel) {
    navigation(
        startDestination = Screen.Welcome.route,
        route = "auth_flow"
    ) {
        composable(route = Screen.Welcome.route) {
            WelcomeScreen(
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                },
                onJoinClick = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }

        composable(route = Screen.SignUp.route) {
            RegisterScreen(
                onJoinClick = { email, password, name, phone, zipCode ->
                    // Handle sign up logic
                    // For now, navigate to login or main screen
                    // You can pass data to ViewModel here
                    println("Sign Up: $email, $name, $phone, $zipCode")

                    // After successful signup, navigate to login or main screen
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Welcome.route) {
                            inclusive = false
                        }
                    }
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Welcome.route) {
                            inclusive = false
                        }
                    }
                }
            )
        }

        composable(route = Screen.Login.route) {
            LoginScreen(
                onLoginClick = { email, password ->
                    // Handle login logic
                    println("Login: $email")

                    authViewModel.login("", "")

                    // After successful login, navigate to main screen
//                    navController.navigate(Screen.Home.route) {
//                        popUpTo(Screen.Welcome.route) {
//                            inclusive = true
//                        }
//                    }
                },
                onSignUpClick = {
                    navController.navigate(Screen.SignUp.route)
                },
                onSkipClick = {
                    // Navigate to main screen without login

                    authViewModel.login("", "")

                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

    }
}

fun NavGraphBuilder.addressGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.LocationMap.route,
        route = "address_flow"
    ) {

        composable(route = Screen.LocationMap.route) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("address_flow")
            }

            val locationViewModel: LocationViewModel = hiltViewModel(parentEntry)

            LocationMapScreen(
                locationViewModel = locationViewModel,
                onBackClick = { navController.navigateUp() },
                onConfirmLocation = { locationData ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "location", locationData
                    )
                    navController.navigate(Screen.AddAddressDetailsScreen.route)
                }
            )
        }

        composable(route = Screen.AddAddressDetailsScreen.route) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("address_flow")
            }

            val locationViewModel: LocationViewModel = hiltViewModel(parentEntry)

            val location = navController.previousBackStackEntry
                ?.savedStateHandle?.get<LocationData>("location")

            val addressIdToEdit =
                backStackEntry.arguments?.getString("addressId")?.toIntOrNull() ?: 0

            if (location != null || addressIdToEdit != 0) {
                AddAddressDetailsScreen(
                    location = location,
                    addressId = addressIdToEdit,
                    locationViewModel = locationViewModel,
                    onBackClick = { navController.navigateUp() },
                    onSaveAddress = {
                        navController.navigateUp()
                    }
                )
            }
        }

        composable(route = Screen.SavedAddressesScreen.route) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("address_flow")
            }

            val locationViewModel: LocationViewModel = hiltViewModel(parentEntry)

            SavedAddressesScreen(
                locationViewModel = locationViewModel,
                onBackClick = { navController.navigateUp() },
                onAddAddressClick = {
                    navController.navigate(Screen.LocationMap.route)
                },
                onEditAddress = { addressDto ->
                    navController.navigate(
                        Screen.AddAddressDetailsScreen.createRoute(addressDto.id)
                    )
                },
            )
        }
    }
}

fun NavGraphBuilder.profileGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel
) {
    navigation(
        startDestination = Screen.Welcome.route,
        route = "profile_flow"
    ) {
        composable(route = Screen.Profile.route) {
            ProfileScreen(
                profileViewModel = profileViewModel,
                onBackClick = { navController.navigateUp() },
                onOrderHistoryClick = { navController.navigate(Screen.MyOrders.route) },
                onEditProfileClick = { navController.navigate(Screen.EditProfile.route) },
                onNotificationsClick = { navController.navigate(Screen.Notifications.route) },
                onReferAndEarnClick = { navController.navigate(Screen.ReferAndEarn.route) },
                onSavedAddressClick = { navController.navigate(Screen.SavedAddressesScreen.route) },
                onLogoutClick = { authViewModel.logout() }
            )
        }

        composable(route = Screen.MyOrders.route) {
            MyOrdersScreen(
                onBackClick = { navController.navigateUp() },
                onCartClick = { navController.navigate("cart") },
                onStartOrderingClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.EditProfile.route) {
            EditProfileScreen(
//                userName = userViewModel.userName,
//                userEmail = userViewModel.userEmail,
                onBackClick = { navController.navigateUp() },
                onSaveClick = { name, email, phone, address ->
//                    userViewModel.updateProfile(name, email, phone, address)
                    navController.navigateUp()
                }
            )
        }

        composable(Screen.Notifications.route) {
            NotificationScreen(
                notifications = getSampleNotifications(),
                onBackClick = { navController.navigateUp() },
                onNotificationClick = { notification ->
                    // Mark as read and navigate to details

//                    navController.navigate("order_detail/${notification.id}")
                },
                onClearAllClick = {
                    // Clear all notifications
                }
            )
        }

        composable(Screen.ReferAndEarn.route) {
            ReferAndEarnScreen(
                onBackClick = { navController.navigateUp() },
                onShareClick = {
                    // Share via system share sheet
                },
                onCopyCodeClick = {
                    // Copy to clipboard
                }
            )
        }

        /*
        // Language
        composable("language") {
            LanguageScreen(
                languages = getSampleLanguages(),
                onBackClick = { navController.navigateUp() },
                onLanguageSelect = { language ->
                    // Save language preference
                    navController.navigateUp()
                }
            )
        }
        */
    }
}

fun NavGraphBuilder.restaurantGraph(
    navController: NavHostController,
    cartViewModel: CartViewModel
) {
    val restaurantGraphRoute = "restaurant_flow?restaurantId={restaurantId}"
    navigation(
        startDestination = Screen.RestaurantList.route,
        route = restaurantGraphRoute
    ) {

        composable(route = Screen.RestaurantList.route) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(restaurantGraphRoute)
            }
            val restaurantViewModel: RestaurantViewModel = hiltViewModel(parentEntry)

            RestaurantListScreen(
                restaurantViewModel = restaurantViewModel,
                onBackClick = { navController.navigateUp() },
                onRestaurantClick = { restaurant ->
                    restaurantViewModel.selectRestaurant(restaurant)
                    navController.navigate(Screen.Restaurant.route)
                }
            )
        }

        composable(
            route = Screen.Restaurant.route,
            arguments = listOf(navArgument("restaurantId") {
                type = NavType.StringType
                defaultValue = ""
                nullable = true
            })
        ) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(restaurantGraphRoute)
            }
            val restaurantViewModel: RestaurantViewModel = hiltViewModel(parentEntry)

            RestaurantScreen(
                restaurantViewModel = restaurantViewModel,
                cartViewModel = cartViewModel,
                onBackClick = { navController.navigateUp() },
                onViewCart = {
                    navController.navigate(Screen.Cart.route)
                }
            )
        }
    }
}

