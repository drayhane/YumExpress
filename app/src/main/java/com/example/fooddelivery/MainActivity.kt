package com.example.fooddelivery

import DisplayFavorits
import DisplayOrders
import Displaydetail

import android.Manifest
import AddReviewUseCase
import GetRestoUsecase
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fooddelivery.data.local.OrderEntity
import com.example.fooddelivery.domain.respository.ReviewRespositoryImpl
import com.example.fooddelivery.domain.respository.UserRepository
import com.example.fooddelivery.domain.respository.UserRepositoryImpl
import com.example.fooddelivery.ui.screens.AddressScreen
import com.example.fooddelivery.ui.screens.DeliverySuccessScreen
import com.example.fooddelivery.ui.screens.TrackingScreen
import com.example.fooddelivery.ui.theme.FoodDeliveryTheme
import reviewRespositoryImpl
import restoRepositoryImpl
import com.example.fooddelivery.domain.usecase.GetReviewUseCase
import com.example.fooddelivery.ui.screens.DisplayEdit
import com.example.fooddelivery.ui.screens.DisplayPanier
import com.example.fooddelivery.ui.screens.DisplayProfil
import com.example.fooddelivery.ui.screens.Displaymeal
import com.example.fooddelivery.ui.screens.RestaurantScreen


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
        val repositoryy = ReviewRespositoryImpl()
        val getReviewUseCase = GetReviewUseCase(repositoryy)
        val userId = "1"
        val userRepository: UserRepository = UserRepositoryImpl()
        val itemid="2"
        val sampleOrders = listOf(
            OrderEntity(
                id = 1,
                restaurantName = "Casbah Istanbul",
                totalPrice = "1495 DA | 2 products",
                date = "12/02/24 17:43",
                status = "Delivered",
                imageRes = R.drawable.casbah_image // Remplace par ton image
            ),
            OrderEntity(
                id = 2,
                restaurantName = "ALOHA",
                totalPrice = "5000 DA | 1 product",
                date = "12/02/24 17:43",
                status = "Canceled",
                imageRes = R.drawable.aloha_image // Remplace par ton image
            ),
            OrderEntity(
                id = 3,
                restaurantName = "Patisserie",
                totalPrice = "700 DA | 3 products",
                date = "12/02/24 17:43",
                status = "Delivered",
                imageRes = R.drawable.patisserie_image // Remplace par ton image
            )

        )
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
                        startDestination = "panier"
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
                        composable("Orders") {
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
                        composable("RestaurantScreen") {
                            RestaurantScreen(
                                addReviewUseCase = addReviewUseCase,
                                restaurantId = "1",
                                getReviewUseCase = getReviewUseCase
                            )
                        }
                        composable ("Profil")   {

                            DisplayProfil(navController, userId)

                        }
                        composable("EditProfil"){DisplayEdit(navController,userId)}
                        composable("Orders")    {DisplayOrders(navController,orders = sampleOrders)}
                        composable("details")   {Displaydetail(navController)}
                        composable("favorits")  {DisplayFavorits(navController)}
                        composable("meal")      {Displaymeal(navController,itemid)}
                        composable("panier")    {DisplayPanier(navController)}

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