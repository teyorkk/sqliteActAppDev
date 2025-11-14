package com.example.sqliteactappdev.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.sqliteactappdev.repository.UserRepository
import com.example.sqliteactappdev.screens.LoginScreen
import com.example.sqliteactappdev.screens.MainScreen
import com.example.sqliteactappdev.screens.RegisterScreen
import com.example.sqliteactappdev.viewmodel.LoginViewModel
import com.example.sqliteactappdev.viewmodel.MainViewModel
import com.example.sqliteactappdev.viewmodel.RegisterViewModel
import com.example.sqliteactappdev.viewmodel.ViewModelFactory

@Composable
fun AppNavigation(
    navController: NavHostController,
    userRepository: UserRepository
) {
    val viewModelFactory = ViewModelFactory(userRepository)

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = viewModel(factory = viewModelFactory)
            
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { user ->
                    navController.navigate(Screen.Main.createRoute(user.id, user.username, user.role)) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = viewModel(factory = viewModelFactory),
                onRegisterSuccess = { user ->
                    navController.navigate(Screen.Main.createRoute(user.id, user.username, user.role)) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Main.route,
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
                navArgument("username") { type = NavType.StringType },
                navArgument("role") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val mainViewModel: MainViewModel = viewModel(factory = viewModelFactory)
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val role = backStackEntry.arguments?.getString("role") ?: "guest"
            
            LaunchedEffect(userId, username, role) {
                if (userId > 0) {
                    mainViewModel.setCurrentUser(
                        com.example.sqliteactappdev.model.User(userId, username, "", role)
                    )
                }
            }
            
            MainScreen(
                viewModel = mainViewModel,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }
    }
}

