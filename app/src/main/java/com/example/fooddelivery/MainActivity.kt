package com.example.fooddelivery

import GetRestoUsecase
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.fooddelivery.ui.screens.RestaurantDetailsScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fooddelivery.domain.respository.ReviewRespository
import com.example.fooddelivery.domain.respository.ReviewRespositoryImpl
import com.example.fooddelivery.ui.screens.AddressScreen
import com.example.fooddelivery.ui.screens.DeliverySuccessScreen
import com.example.fooddelivery.ui.screens.TrackingScreen
import com.example.fooddelivery.ui.theme.FoodDeliveryTheme
import restoRepositoryImpl
import com.example.fooddelivery.domain.usecase.GetReviewUseCase



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = restoRepositoryImpl()
        val getCountriesUseCase = GetRestoUsecase(repository)
        val repositoryy = ReviewRespositoryImpl()
        val getReviewUseCase = GetReviewUseCase(repositoryy)

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
                        startDestination = "RestaurantScreen"
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

                            TrackingScreen(navController = navController, endPointLat = lat, endPointLon = lon)
                        }
                        composable("address_screen") {
                            AddressScreen(
                                context = this@MainActivity,
                                navController = navController
                            )
                        }
                        composable("DeliverySuccessScreen"){
                            DeliverySuccessScreen(
                                navController=navController
                            )
                        }
                        composable("RestaurantScreen"){
                            RestaurantDetailsScreen("1", getReviewUseCase = getReviewUseCase )
                        }

                    }


                }
            }
        }
    }
}
