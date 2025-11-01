package com.eatfair.app.ui.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object SignUp : Screen("signup")
    object Login : Screen("login")
    object Home: Screen("home")
    object Search: Screen("search")
    object Profile: Screen("profile")
    object MyOrders: Screen("my_orders")
    object EditProfile: Screen("edit_profile")
    object ReferAndEarn: Screen("refer_and_earn")
    object Notifications: Screen("notification")

    object RestaurantFlow: Screen("restaurant_flow"){
        fun createRoute(restaurantId: String) = "restaurant/$restaurantId"
    }
    object RestaurantList: Screen("restaurant_list")

    object Restaurant: Screen("restaurant/{restaurantId}"){
        fun createRoute(restaurantId: String) = "restaurant/$restaurantId"
    }
    object Cart: Screen("cart")

    object LocationMap: Screen("location_map")

    object AddAddressDetailsScreen: Screen("add_address_details?addressId={addressId}") {
        fun createRoute(addressId: Int) = "add_address_details?addressId=$addressId"
    }

    object SavedAddressesScreen: Screen("saved_addresses")

    object OrderTrackingScreen: Screen("order_tracking?orderId={orderId}") {
        fun createRoute(orderId: String) = "order_tracking?orderId=$orderId"
    }

}