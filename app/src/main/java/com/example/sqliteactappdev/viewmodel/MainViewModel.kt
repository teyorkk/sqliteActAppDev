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
    val isLoading: Boolean = false,
    val currentUser: User? = null,
    val selectedUser: User? = null,
    val showUserDialog: Boolean = false
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

    fun setCurrentUser(user: User) {
        _uiState.value = _uiState.value.copy(currentUser = user)
    }

    fun togglePasswordVisibility(userId: Int) {
        val currentUser = _uiState.value.currentUser
        // Only allow password visibility toggle if user is admin or viewing their own password
        if (currentUser != null && (currentUser.role == "admin" || currentUser.id == userId)) {
            val currentVisibility = _uiState.value.passwordVisibility.toMutableMap()
            currentVisibility[userId] = !(currentVisibility[userId] ?: false)
            _uiState.value = _uiState.value.copy(passwordVisibility = currentVisibility)
        }
    }

    fun canViewPassword(userId: Int): Boolean {
        val currentUser = _uiState.value.currentUser
        return currentUser != null && (currentUser.role == "admin" || currentUser.id == userId)
    }

    fun canEditUser(): Boolean {
        return _uiState.value.currentUser?.role == "admin"
    }

    fun canDeleteUser(): Boolean {
        return _uiState.value.currentUser?.role == "admin"
    }

    fun showUserDialog(user: User) {
        _uiState.value = _uiState.value.copy(
            selectedUser = user,
            showUserDialog = true
        )
    }

    fun hideUserDialog() {
        _uiState.value = _uiState.value.copy(
            selectedUser = null,
            showUserDialog = false
        )
    }

    fun updateUser(id: Int, username: String, password: String, role: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = userRepository.updateUser(id, username, password, role)
            _uiState.value = _uiState.value.copy(isLoading = false)
            
            result.fold(
                onSuccess = {
                    loadUsers()
                    hideUserDialog()
                    onSuccess()
                },
                onFailure = {
                    // Handle error if needed
                }
            )
        }
    }

    fun deleteUser(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = userRepository.deleteUser(id)
            _uiState.value = _uiState.value.copy(isLoading = false)
            
            result.fold(
                onSuccess = {
                    loadUsers()
                    hideUserDialog()
                    onSuccess()
                },
                onFailure = {
                    // Handle error if needed
                }
            )
        }
    }
}

