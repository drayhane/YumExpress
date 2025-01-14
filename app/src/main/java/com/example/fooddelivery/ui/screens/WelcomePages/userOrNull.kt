package com.example.fooddelivery.ui.screens.WelcomePages

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import io.github.jan.supabase.auth.auth
import supabaseClient
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun userOrNull(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val isFirstLaunch = sharedPreferences.getBoolean("is_first_launch", true)
    val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)

    // Use LaunchedEffect to navigate once the checks are complete
    LaunchedEffect(Unit) {
        when {
            isFirstLaunch -> {
                // Mark first launch as false for future app starts
                sharedPreferences.edit().putBoolean("is_first_launch", false).apply()

                // Navigate to the onboarding screen
                navController.navigate("Welcome1") {
                    popUpTo("userOrNull") { inclusive = true }
                }
            }
            isLoggedIn -> {
                // User is logged in, navigate to the main screen
                navController.navigate("HomeScreen") {
                    popUpTo("userOrNull") { inclusive = true }
                }
            }
            else -> {
                // User is not logged in, navigate to the login screen
                navController.navigate("LogoPage") {
                    popUpTo("userOrNull") { inclusive = true }
                }
            }
        }
    }

    // Show a loading indicator while checking the statuses
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}