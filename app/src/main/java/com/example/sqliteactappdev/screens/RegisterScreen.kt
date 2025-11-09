package com.example.sqliteactappdev.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sqliteactappdev.ui.components.*
import com.example.sqliteactappdev.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
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
            title = "Create Account",
            subtitle = "Sign up to get started"
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
                .padding(bottom = 16.dp),
            enabled = !uiState.isLoading
        )

        PasswordTextField(
            value = uiState.confirmPassword,
            onValueChange = viewModel::updateConfirmPassword,
            label = "Confirm Password",
            isPasswordVisible = uiState.confirmPasswordVisible,
            onPasswordVisibilityToggle = viewModel::toggleConfirmPasswordVisibility,
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
            text = "Register",
            onClick = { viewModel.register(onRegisterSuccess) },
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
                text = "Already have an account? ",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            TextButton(onClick = onNavigateToLogin) {
                Text(
                    text = "Login",
                    color = com.example.sqliteactappdev.ui.theme.OrangePrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
