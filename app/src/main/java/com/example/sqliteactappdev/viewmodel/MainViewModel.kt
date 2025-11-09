package com.example.sqliteactappdev.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sqliteactappdev.model.User
import com.example.sqliteactappdev.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MainUiState(
    val users: List<User> = emptyList(),
    val passwordVisibility: Map<Int, Boolean> = emptyMap(),
    val isLoading: Boolean = false
)

class MainViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val users = userRepository.getAllUsers()
            _uiState.value = _uiState.value.copy(
                users = users,
                isLoading = false
            )
        }
    }

    fun togglePasswordVisibility(userId: Int) {
        val currentVisibility = _uiState.value.passwordVisibility.toMutableMap()
        currentVisibility[userId] = !(currentVisibility[userId] ?: false)
        _uiState.value = _uiState.value.copy(passwordVisibility = currentVisibility)
    }
}

