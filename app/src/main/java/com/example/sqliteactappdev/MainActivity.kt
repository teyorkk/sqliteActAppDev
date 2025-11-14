package com.example.sqliteactappdev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.sqliteactappdev.database.UserDatabaseHelper
import com.example.sqliteactappdev.navigation.AppNavigation
import com.example.sqliteactappdev.repository.UserRepository
import com.example.sqliteactappdev.repository.UserRepositoryImpl
import com.example.sqliteactappdev.ui.theme.SqliteActAppDevTheme

class MainActivity : ComponentActivity() {
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize repository
        val dbHelper = UserDatabaseHelper(this)
        userRepository = UserRepositoryImpl(dbHelper)
        
        setContent {
            SqliteActAppDevTheme {
                val navController = rememberNavController()
                AppNavigation(
                    navController = navController,
                    userRepository = userRepository
                    )
                }
            }
        }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    SqliteActAppDevTheme {
        // Preview placeholder
    }
}