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
import com.example.fooddelivery.ui.screens.RestaurantScreen
import com.example.fooddelivery.ui.theme.FoodDeliveryTheme
import restoRepositoryImpl

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = restoRepositoryImpl()
        val getCountriesUseCase = GetRestoUsecase(repository)

        // Set up the theme and UI content of the app
        setContent {
            FoodDeliveryTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Set the Main Screen content (e.g., a list of countries)
                    RestaurantScreen()
                }
            }
        }
    }
}
