package com.example.fooddelivery.ui.screens.WelcomePages

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fooddelivery.R
import kotlinx.coroutines.delay

@SuppressLint("RememberReturnType")
@Composable
fun LogoPage(navController: NavController) {
    // State to control visibility
    val isLogoVisible = remember { mutableStateOf(false) }

    // Animate the alpha value (opacity)
    val alpha = animateFloatAsState(
        targetValue = if (isLogoVisible.value) 1f else 0f,  // Use `.value` here to access the state
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 2000)
    )

    // Trigger to make the logo visible

    // Set the logo to fade in after the screen is loaded
    LaunchedEffect(Unit) {
        isLogoVisible.value = true
        delay(2500)
        navController.navigate("LogIn")
    }

    Box(
        modifier = Modifier
            .padding(60.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo), // Replace with your logo resource
            contentDescription = "Logo",
            modifier = Modifier.fillMaxSize(),
            alpha = alpha.value // Apply the animated alpha value to the image
        )
    }
}

