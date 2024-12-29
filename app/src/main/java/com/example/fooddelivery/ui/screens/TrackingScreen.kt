package com.example.fooddelivery.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.fooddelivery.ui.components.BackToOrderCard
import com.example.fooddelivery.ui.components.OpenStreetMapComposable
import com.example.fooddelivery.ui.components.TrackCard
import org.osmdroid.util.GeoPoint
@Composable
fun TrackingScreen(navController: NavController, endPointLat: Double, endPointLon: Double) {
    val endPoint = GeoPoint(endPointLat, endPointLon)
    val context= LocalContext.current
    LaunchedEffect(Unit) {
        println("Received Lat: $endPointLat, Lon: $endPointLon")
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier
            .fillMaxSize()

        ) {
            // OpenStreetMap as the background layer
            OpenStreetMapComposable(
                context = LocalContext.current,
                startPoint = GeoPoint(36.77165290000001, 5.606316820071114),
                endPoint = endPoint,
                modifier = Modifier.fillMaxSize()
            )

            Column(
            modifier = Modifier
                .padding(top = 16.dp)
                .zIndex(1f)
        ) {
            BackToOrderCard(
                navController = navController,
                modifier = Modifier.padding(16.dp)
            )
        }
        }

        // TrackCard remains outside the Box at the bottom of the screen
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            TrackCard(
                 "12:30",
                "Derbal Zakaria",
                "0686544587",
                 "",
                "ESI",
                 context,
                navController
            )
        }
    }
}


