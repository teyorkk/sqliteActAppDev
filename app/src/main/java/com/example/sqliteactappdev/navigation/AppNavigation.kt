package com.example.sqliteactappdev.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
            LoginScreen(
                viewModel = viewModel(factory = viewModelFactory),
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
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
                onRegisterSuccess = {
                    navController.navigate(Screen.Main.route) {
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

        composable(Screen.Main.route) {
            MainScreen(
                viewModel = viewModel(factory = viewModelFactory),
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }
    }
}

