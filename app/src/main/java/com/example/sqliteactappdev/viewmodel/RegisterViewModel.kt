package com.example.sqliteactappdev.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sqliteactappdev.repository.UserRepository
import com.example.sqliteactappdev.util.ValidationResult
import com.example.sqliteactappdev.util.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val errorMessage: String = "",
    val isLoading: Boolean = false
)

class RegisterViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun updateUsername(username: String) {
        _uiState.value = _uiState.value.copy(
            username = username,
            errorMessage = ""
        )
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            errorMessage = ""
        )
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(
            confirmPassword = confirmPassword,
            errorMessage = ""
        )
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(passwordVisible = !_uiState.value.passwordVisible)
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.value = _uiState.value.copy(confirmPasswordVisible = !_uiState.value.confirmPasswordVisible)
    }

    fun register(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = "")

            // Validate all fields
            val usernameValidation = ValidationUtils.validateUsername(_uiState.value.username)
            val passwordValidation = ValidationUtils.validatePassword(_uiState.value.password)
            val passwordMatchValidation = ValidationUtils.validatePasswordMatch(
                _uiState.value.password,
                _uiState.value.confirmPassword
            )

            val error = when {
                usernameValidation is ValidationResult.Error -> usernameValidation.message
                passwordValidation is ValidationResult.Error -> passwordValidation.message
                passwordMatchValidation is ValidationResult.Error -> passwordMatchValidation.message
                else -> null
            }

            if (error != null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error
                )
                return@launch
            }

            // Check if username already exists
            val existingUser = userRepository.getUserByUsername(_uiState.value.username)
            if (existingUser != null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Username already exists"
                )
                return@launch
            }

            // Insert user
            val result = userRepository.insertUser(_uiState.value.username, _uiState.value.password)
            
            _uiState.value = _uiState.value.copy(isLoading = false)
            
            result.fold(
                onSuccess = { onSuccess() },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Registration failed. Please try again."
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = "")
    }
}

