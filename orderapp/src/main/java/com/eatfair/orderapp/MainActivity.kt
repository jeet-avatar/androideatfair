package com.eatfair.orderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.eatfair.orderapp.constants.BottomNavItems
import com.eatfair.orderapp.ui.navigation.AppRoutes
import com.eatfair.orderapp.ui.navigation.NavigationGraph
import com.eatfair.orderapp.ui.navigation.authGraph
import com.eatfair.orderapp.ui.screens.auth.AuthState
import com.eatfair.orderapp.ui.screens.auth.AuthViewModel
import com.eatfair.orderapp.ui.theme.EFAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authViewModel: AuthViewModel by viewModels()

        installSplashScreen().apply {
            // Keep the splash screen on-screen until the auth state is determined.
            // It stays as long as the authState is Initial or Loading.
            setKeepOnScreenCondition {
                authViewModel.authState.value is AuthState.Initial ||
                        authViewModel.authState.value is AuthState.Loading
            }
        }

        enableEdgeToEdge()
        setContent {
            EFAppTheme {
                RootApp(authViewModel)
            }
        }
    }
}

@Composable
fun RootApp(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    val authState = authViewModel.authState.collectAsState().value

    if (authState !is AuthState.Initial && authState !is AuthState.Loading) {
        NavHost(
            navController = navController,
            startDestination = if (authState is AuthState.Authenticated) {
                AppRoutes.MAIN_GRAPH
            } else {
                AppRoutes.AUTH_GRAPH
            }
        ) {
            authGraph(navController, authViewModel)

            composable(route = AppRoutes.MAIN_GRAPH) {
                MainAppContent()
            }
        }
    }
}

@Composable
fun MainAppContent() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            BottomNavItems.entries.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            painter = painterResource(id = destination.iconResId),
                            contentDescription = destination.label
                        )
                    },
                    label = { Text(destination.label) },
                    selected = currentRoute == destination.name.lowercase(),
                    onClick = {
                        navController.navigate(destination.name.lowercase()) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) {
        NavigationGraph(navController)
    }
}