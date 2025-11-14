package com.example.sqliteactappdev.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sqliteactappdev.model.User
import com.example.sqliteactappdev.ui.components.PasswordTextField
import com.example.sqliteactappdev.ui.components.UsernameTextField
import com.example.sqliteactappdev.ui.theme.OrangePrimary
import com.example.sqliteactappdev.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = if (uiState.currentUser?.role == "admin") "Admin View" else "Guest View",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            },
            actions = {
                IconButton(onClick = onLogout) {
                    Icon(
                        imageVector = Icons.Filled.ExitToApp,
                        contentDescription = "Logout",
                        tint = OrangePrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(OrangePrimary)
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Name",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Role",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Password",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        if (uiState.users.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No users found",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(
                    items = uiState.users,
                    key = { user -> user.id }
                ) { user ->
                    UserRow(
                        user = user,
                        onClick = { viewModel.showUserDialog(user) }
                    )
                }
            }
        }
    }

    // User Detail Dialog
    if (uiState.showUserDialog && uiState.selectedUser != null) {
        val selectedUser = uiState.selectedUser!!
        val canViewPassword = viewModel.canViewPassword(selectedUser.id)
        val canEdit = viewModel.canEditUser()
        val canDelete = viewModel.canDeleteUser()
        
        UserDetailDialog(
            user = selectedUser,
            currentUser = uiState.currentUser,
            isPasswordVisible = uiState.passwordVisibility[selectedUser.id] ?: false,
            canViewPassword = canViewPassword,
            canEdit = canEdit,
            canDelete = canDelete,
            onPasswordVisibilityToggle = { viewModel.togglePasswordVisibility(selectedUser.id) },
            onDismiss = { viewModel.hideUserDialog() },
            onUpdate = { username, password, role ->
                viewModel.updateUser(selectedUser.id, username, password, role) {}
            },
            onDelete = {
                viewModel.deleteUser(selectedUser.id) {}
            }
        )
    }
}

@Composable
fun UserRow(
    user: User,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = OrangePrimary.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Username Column
                    Text(
                        text = user.username,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )

                    // Role Column
                    Text(
                        text = user.role,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )

                    // Password Column (always hidden on main page)
                    Text(
                        text = "*".repeat(user.password.length),
                        modifier = Modifier.weight(1f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun UserDetailDialog(
    user: User,
    currentUser: User?,
    isPasswordVisible: Boolean,
    canViewPassword: Boolean,
    canEdit: Boolean,
    canDelete: Boolean,
    onPasswordVisibilityToggle: () -> Unit,
    onDismiss: () -> Unit,
    onUpdate: (String, String, String) -> Unit,
    onDelete: () -> Unit
) {
    val isAdmin = currentUser?.role == "admin"
    var isEditing by remember { mutableStateOf(false) }
    var editUsername by remember { mutableStateOf(user.username) }
    var editPassword by remember { mutableStateOf(user.password) }
    var editRole by remember { mutableStateOf(user.role) }
    var editPasswordVisible by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (isEditing) "Edit User" else "User Details",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = OrangePrimary
                )

                if (isEditing && isAdmin) {
                    UsernameTextField(
                        value = editUsername,
                        onValueChange = { editUsername = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    PasswordTextField(
                        value = editPassword,
                        onValueChange = { editPassword = it },
                        label = "Password",
                        isPasswordVisible = editPasswordVisible,
                        onPasswordVisibilityToggle = { editPasswordVisible = !editPasswordVisible },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Role Selection
                    Column {
                        Text(
                            text = "Role",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = editRole == "guest",
                                    onClick = { editRole = "guest" }
                                )
                                Text(text = "Guest", modifier = Modifier.padding(start = 4.dp))
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = editRole == "admin",
                                    onClick = { editRole = "admin" }
                                )
                                Text(text = "Admin", modifier = Modifier.padding(start = 4.dp))
                            }
                        }
                    }
                } else {
                    // View Mode
                    DetailRow("Username", user.username)
                    DetailRow("Role", user.role)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Password:",
                            fontWeight = FontWeight.Medium
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (canViewPassword && isPasswordVisible) user.password else "*".repeat(user.password.length),
                                modifier = Modifier.padding(end = if (canViewPassword) 8.dp else 0.dp)
                            )
                            if (canViewPassword) {
                                IconButton(onClick = onPasswordVisibilityToggle) {
                                    Icon(
                                        imageVector = if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                        contentDescription = if (isPasswordVisible) "Hide" else "Show",
                                        tint = OrangePrimary
                                    )
                                }
                            }
                        }
                    }
                }

                // Action Buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (canEdit) {
                        if (isEditing) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        onUpdate(editUsername, editPassword, editRole)
                                        isEditing = false
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                                ) {
                                    Text("Save")
                                }
                                OutlinedButton(
                                    onClick = {
                                        isEditing = false
                                        editUsername = user.username
                                        editPassword = user.password
                                        editRole = user.role
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Cancel")
                                }
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { isEditing = true },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                                ) {
                                    Icon(
                                        Icons.Filled.Edit,
                                        contentDescription = "Edit",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Edit")
                                }
                                if (canDelete) {
                                    Button(
                                        onClick = {
                                            onDelete()
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                                    ) {
                                        Icon(
                                            Icons.Filled.Delete,
                                            contentDescription = "Delete",
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Delete")
                                    }
                                }
                            }
                        }
                    }
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            fontWeight = FontWeight.Medium
        )
        Text(text = value)
    }
}
