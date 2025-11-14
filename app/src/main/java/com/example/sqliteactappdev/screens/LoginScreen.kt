package com.example.sqliteactappdev.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sqliteactappdev.ui.components.*
import com.example.sqliteactappdev.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: (com.example.sqliteactappdev.model.User) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ScreenTitle(
            title = "Log in",
            subtitle = "Sign in to continue"
        )

        UsernameTextField(
            value = uiState.username,
            onValueChange = viewModel::updateUsername,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            enabled = !uiState.isLoading
        )

        PasswordTextField(
            value = uiState.password,
            onValueChange = viewModel::updatePassword,
            label = "Password",
            isPasswordVisible = uiState.passwordVisible,
            onPasswordVisibilityToggle = viewModel::togglePasswordVisibility,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            enabled = !uiState.isLoading
        )

        ErrorMessage(
            message = uiState.errorMessage,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        PrimaryButton(
            text = "Login",
            onClick = { 
                viewModel.login {
                    uiState.loggedInUser?.let { onLoginSuccess(it) }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = 16.dp),
            enabled = !uiState.isLoading
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don't have an account?",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            TextButton(
                onClick = onNavigateToRegister,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Register",
                    color = com.example.sqliteactappdev.ui.theme.OrangePrimary,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                )
            }
        }
    }
}
