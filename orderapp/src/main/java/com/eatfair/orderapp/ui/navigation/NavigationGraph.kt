package com.eatfair.orderapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.eatfair.orderapp.ui.screens.auth.AuthViewModel
import com.eatfair.orderapp.ui.screens.auth.LoginScreen
import com.eatfair.orderapp.ui.screens.auth.RegisterScreen
import com.eatfair.orderapp.ui.screens.auth.WelcomeScreen
import com.eatfair.orderapp.ui.screens.earnings.EarningsHomeScreen
import com.eatfair.orderapp.ui.screens.earnings.EarningsViewModel
import com.eatfair.orderapp.ui.screens.home.HomeScreen
import com.eatfair.orderapp.ui.screens.home.HomeViewModel
import com.eatfair.orderapp.ui.screens.orders.OrderDetailScreen
import com.eatfair.orderapp.ui.screens.orders.OrdersScreen
import com.eatfair.orderapp.ui.screens.orders.OrdersViewModel
import com.eatfair.orderapp.ui.screens.profile.ProfileScreen
import com.eatfair.orderapp.ui.screens.profile.ProfileViewModel
import com.eatfair.orderapp.ui.screens.profile.menuScreen.ChangePasswordScreen
import com.eatfair.orderapp.ui.screens.profile.menuScreen.DriveLicenseScreen
import com.eatfair.orderapp.ui.screens.profile.menuScreen.PersonalInfoScreen
import com.eatfair.orderapp.ui.screens.profile.menuScreen.PrivacyPolicyScreen
import com.eatfair.orderapp.ui.screens.profile.menuScreen.RatingScreen
import com.eatfair.orderapp.ui.screens.profile.menuScreen.SettingsScreen
import com.eatfair.orderapp.ui.screens.profile.menuScreen.SupportFAQScreen
import com.eatfair.orderapp.ui.screens.profile.menuScreen.VehicleRegistrationFormScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(route = Screen.Home.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = homeViewModel,
            )
        }

        composable(route = Screen.Orders.route) {
            val viewModel: OrdersViewModel = hiltViewModel()
            OrdersScreen(
                onNavigateToDetail = { orderId ->
                    navController.navigate(Screen.OrderDetail.createRoute(orderId))
                },
                viewModel = viewModel
            )
        }

        composable(
            route = Screen.OrderDetail.route, arguments = listOf(
                navArgument("orderId") { type = NavType.StringType }
            )) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("orders")
            }

            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            val viewModel: OrdersViewModel = hiltViewModel(parentEntry)

            OrderDetailScreen(
                orderId = orderId,
                onNavigateBack = { navController.navigateUp() },
                viewModel = viewModel
            )
        }

        composable(Screen.Earnings.route) {

            val viewModel: EarningsViewModel = hiltViewModel()

            EarningsHomeScreen(viewModel = viewModel)
        }

        profileGraph(navController = navController)

    }
}

fun NavGraphBuilder.authGraph(navController: NavHostController, authViewModel: AuthViewModel) {
    navigation(
        startDestination = Screen.Welcome.route,
        route = AppRoutes.AUTH_GRAPH
    ) {
        composable(route = Screen.Welcome.route) {
            WelcomeScreen(
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                },
                onJoinAsPartnerClick = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(route = Screen.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterClick = {
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
                onBackClick = { navController.navigateUp() },
                onLoginClick = {
                    // Handle login logic
                    authViewModel.login("", "")

                    // After successful login, navigate to main screen
                    navController.navigate(AppRoutes.MAIN_GRAPH) {
                        popUpTo(AppRoutes.AUTH_GRAPH) {
                            inclusive = true
                        }
                    }
                },
                onJoinAsPartnerClick = {
                    navController.navigate(Screen.Register.route)
                },
            )
        }

    }
}

fun NavGraphBuilder.profileGraph(navController: NavHostController) {

    navigation(
        startDestination = ProfileScreen.Profile.route,
        route = AppRoutes.PROFILE_GRAPH
    ) {
        composable(ProfileScreen.Profile.route) {

            val viewModel: ProfileViewModel = hiltViewModel()

            ProfileScreen(
                viewModel = viewModel,
                onNavigateToPersonalInfo = {
                    navController.navigate(ProfileScreen.PersonalInfo.route)
                },
                onNavigateToDriveLicense = {
                    navController.navigate(ProfileScreen.DriveLicense.route)
                },
                onNavigateToVehicleReg = { type ->
                    navController.navigate(ProfileScreen.VehicleRegForm.createRoute(type))
                },
                onNavigateToChangePassword = {
                    navController.navigate(ProfileScreen.ChangePassword.route)
                },
                onNavigateToRating = {
                    navController.navigate(ProfileScreen.Rating.route)
                },
                onNavigateToSettings = {
                    navController.navigate(ProfileScreen.Settings.route)
                },
                onNavigateToSupport = {
                    navController.navigate(ProfileScreen.SupportFAQ.route)
                },
                onLogout = { /* Handle logout */ }
            )
        }

        composable(ProfileScreen.PersonalInfo.route) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(AppRoutes.PROFILE_GRAPH)
            }

            val viewModel: ProfileViewModel = hiltViewModel(parentEntry)

            PersonalInfoScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(ProfileScreen.DriveLicense.route) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(AppRoutes.PROFILE_GRAPH)
            }

            val viewModel: ProfileViewModel = hiltViewModel(parentEntry)

            DriveLicenseScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() })

        }

//        composable(ProfileScreen.VehicleRegMenu.route) {
//            VehicleRegistrationMenuScreen(
//                onNavigateBack = { navController.popBackStack() },
//                onNavigateToBikeReg = {
//                    navController.navigate(
//                        ProfileScreen.VehicleRegForm.createRoute("bike")
//                    )
//                },
//                onNavigateToCarReg = {
//                    navController.navigate(
//                        ProfileScreen.VehicleRegForm.createRoute("car")
//                    )
//                }
//            )
//        }

        composable(
            route = ProfileScreen.VehicleRegForm.route,
            arguments = listOf(
                navArgument("vehicleType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val vehicleType = backStackEntry.arguments?.getString("vehicleType") ?: "bike"
            VehicleRegistrationFormScreen(
                vehicleType = vehicleType,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(ProfileScreen.ChangePassword.route) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(AppRoutes.PROFILE_GRAPH)
            }

            val viewModel: ProfileViewModel = hiltViewModel(parentEntry)

            ChangePasswordScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(ProfileScreen.Rating.route) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(AppRoutes.PROFILE_GRAPH)
            }

            val viewModel: ProfileViewModel = hiltViewModel(parentEntry)

            RatingScreen(
//                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(ProfileScreen.Settings.route) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(AppRoutes.PROFILE_GRAPH)
            }

            val viewModel: ProfileViewModel = hiltViewModel(parentEntry)

            SettingsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPrivacyPolicy = {
                    navController.navigate(ProfileScreen.PrivacyPolicy.route)
                },
                onNavigateToTerms = {
                    navController.navigate(ProfileScreen.PrivacyPolicy.route)
                }
            )
        }

        composable(ProfileScreen.SupportFAQ.route) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(AppRoutes.PROFILE_GRAPH)
            }

            val viewModel: ProfileViewModel = hiltViewModel(parentEntry)

            SupportFAQScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(ProfileScreen.PrivacyPolicy.route) {
            PrivacyPolicyScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}