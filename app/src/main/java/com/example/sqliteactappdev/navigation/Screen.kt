package com.example.sqliteactappdev.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Main : Screen("main?userId={userId}&username={username}&role={role}") {
        fun createRoute(userId: Int, username: String, role: String) = "main?userId=$userId&username=$username&role=$role"
    }
}

