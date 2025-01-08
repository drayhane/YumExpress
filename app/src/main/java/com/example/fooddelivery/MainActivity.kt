package com.example.fooddelivery
import androidx.compose.foundation.layout.fillMaxSize


import android.Manifest
import AddReviewUseCase
import GetRestoUsecase
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fooddelivery.domain.respository.ReviewRespositoryImpl
import com.example.fooddelivery.ui.screens.AddressScreen
import com.example.fooddelivery.ui.screens.DeliverySuccessScreen
import com.example.fooddelivery.ui.screens.TrackingScreen
import com.example.fooddelivery.ui.theme.FoodDeliveryTheme
import reviewRespositoryImpl
import restoRepositoryImpl
import com.example.fooddelivery.domain.usecase.GetReviewUseCase
import com.example.fooddelivery.ui.screens.RestaurantScreen

import com.example.fooddelivery.navigation.BottomNavigationBar
import com.example.fooddelivery.navigation.NavGraph
import com.example.fooddelivery.navigationview.NavigationView
import com.example.fooddelivery.ui.screens.HomeScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestPermissions()}

    // Function to check and request necessary permissions
    private fun checkAndRequestPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        // Check location permission
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        // Check notification permission (only for Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionsLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            // All permissions granted, proceed with the app
            startApp()
        }
    }

    // Activity result launcher for permissions
    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val locationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val notificationsGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions[Manifest.permission.POST_NOTIFICATIONS] ?: false
        } else {
            true
        }

        if (locationGranted) {
            startApp()
        } else {
            // If location is denied, show a message and close the app
            showPermissionDeniedDialog()
        }
    }
    private fun startApp() {
        val respo = reviewRespositoryImpl()
        val addReviewUseCase = AddReviewUseCase(respo)
        val restoid = "1"
        val repository = restoRepositoryImpl()
        val getCountriesUseCase = GetRestoUsecase(repository)
        val repositoryy = ReviewRespositoryImpl()
        val getReviewUseCase = GetReviewUseCase(repositoryy)


        // Set up the theme and UI content of the app
        setContent {
            FoodDeliveryTheme {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Navigation logic directly embedded
                    NavHost(
                        navController = navController,
                        startDestination = "Login"
                    ) {
                        composable(
                            route = "tracking_screen?lat={lat}&lon={lon}",
                            arguments = listOf(
                                navArgument("lat") { type = NavType.StringType },
                                navArgument("lon") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val latString = backStackEntry.arguments?.getString("lat") ?: "0.0"
                            val lonString = backStackEntry.arguments?.getString("lon") ?: "0.0"
                            // Convert String to Double
                            val lat = latString.toDouble()
                            val lon = lonString.toDouble()

                            TrackingScreen(
                                navController = navController,
                                endPointLat = lat,
                                endPointLon = lon
                            )
                        }
                        composable("address_screen") {
                            AddressScreen(
                                context = this@MainActivity,
                                navController = navController
                            )
                        }
                        composable("DeliverySuccessScreen") {
                            DeliverySuccessScreen(
                                navController = navController
                            )
                        }
                        composable("RestaurantScreen"){
                            RestaurantScreen(
                                context = this@MainActivity,
                                addReviewUseCase = addReviewUseCase, restaurantId = "1", getReviewUseCase = getReviewUseCase)
                        }
                        composable("HomeScreen"){
                            HomeScreen()

                        }

                        composable("Login"){
                            NavigationView()

                        }

                    }


                }
            }
        }
    }
    private fun showPermissionDeniedDialog() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Permission Required")
        builder.setMessage("L'application nécessite l'accès à la localisation pour fonctionner. Veuillez activer la localisation.")
        builder.setPositiveButton("Quitter") { _, _ ->
            finish() // Close the app
        }
        builder.setCancelable(false)
        builder.show()
    }
}

