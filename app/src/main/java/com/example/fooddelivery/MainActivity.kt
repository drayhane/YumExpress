package com.example.fooddelivery

import AddReviewUseCase
import GetRestoUsecase
import MenuRepositoryImpl
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
import reviewRespository
import reviewRespositoryImpl

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val respo = reviewRespositoryImpl()
        val addReviewUseCase = AddReviewUseCase(respo)
        val restoid ="1"

        // Set up the theme and UI content of the app
        setContent {
            FoodDeliveryTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RestaurantScreen(addReviewUseCase = addReviewUseCase)
                }
            }
        }
    }
}
