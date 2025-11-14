package com.example.sqliteactappdev.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sqliteactappdev.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val errorMessage: String = "",
    val isLoading: Boolean = false,
    val loggedInUser: com.example.sqliteactappdev.model.User? = null
)

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

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

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(passwordVisible = !_uiState.value.passwordVisible)
    }

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = "")
            
            val validation = com.example.sqliteactappdev.util.ValidationUtils.validateLoginFields(
                _uiState.value.username,
                _uiState.value.password
            )
            
            if (validation is com.example.sqliteactappdev.util.ValidationResult.Error) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = validation.message
                )
                return@launch
            }

            val user = userRepository.getUser(_uiState.value.username, _uiState.value.password)
            
            _uiState.value = _uiState.value.copy(isLoading = false)
            
            if (user != null) {
                _uiState.value = _uiState.value.copy(loggedInUser = user)
                onSuccess()
            } else {
                _uiState.value = _uiState.value.copy(errorMessage = "Invalid username or password")
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = "")
    }
}

