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
import com.example.sqliteactappdev.model.User
import com.example.sqliteactappdev.ui.components.*
import com.example.sqliteactappdev.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onRegisterSuccess: (User) -> Unit,
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
            title = "Register",
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
                .padding(bottom = 16.dp),
            enabled = !uiState.isLoading
        )

        // Role Selection
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Text(
                text = "Account Type",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    RadioButton(
                        selected = uiState.role == "guest",
                        onClick = { viewModel.updateRole("guest") },
                        enabled = !uiState.isLoading
                    )
                    Text(
                        text = "Guest",
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    RadioButton(
                        selected = uiState.role == "admin",
                        onClick = { viewModel.updateRole("admin") },
                        enabled = !uiState.isLoading
                    )
                    Text(
                        text = "Admin",
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }

        ErrorMessage(
            message = uiState.errorMessage,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        PrimaryButton(
            text = "Register",
            onClick = { 
                viewModel.register { user ->
                    onRegisterSuccess(user)
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
                text = "Already have an account?",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            TextButton(
                onClick = onNavigateToLogin,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Login",
                    color = com.example.sqliteactappdev.ui.theme.OrangePrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
