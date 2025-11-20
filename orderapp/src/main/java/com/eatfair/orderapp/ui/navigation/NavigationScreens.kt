package com.eatfair.orderapp.ui.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Register : Screen("register")
    object Login : Screen("login")

    object Home: Screen("home")

    object Menu: Screen("menu")

    object Orders: Screen("orders")
    object Earnings: Screen("earnings")

//    object Profile: Screen("profile")
//    object EditProfile: Screen("edit_profile")


    object OrderDetail : Screen("order_detail/{orderId}") {
        fun createRoute(orderId: String) = "order_detail/$orderId"
    }

}

sealed class ProfileScreen(val route: String) {
    object Profile : ProfileScreen("profile")
    object PersonalInfo : ProfileScreen("personal_info")
    object DriveLicense : ProfileScreen("drive_license")
    object VehicleRegForm : ProfileScreen("vehicle_reg_form/{vehicleType}") {
        fun createRoute(vehicleType: String) = "vehicle_reg_form/$vehicleType"
    }
    object ChangePassword : ProfileScreen("change_password")
    object Rating : ProfileScreen("rating")
    object Settings : ProfileScreen("settings")
    object SupportFAQ : ProfileScreen("support_faq")
    object PrivacyPolicy : ProfileScreen("privacy_policy")
    object TermsConditions : ProfileScreen("terms_conditions")
}