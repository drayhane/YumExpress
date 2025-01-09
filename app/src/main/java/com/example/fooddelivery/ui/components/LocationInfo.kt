package com.example.fooddelivery.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.lint.Names.Runtime.LaunchedEffect
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

suspend fun getCurrentLocationName(context: Context): String? {
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // Check permissions
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // If permissions are not granted, return null
        return null
    }

    return suspendCancellableCoroutine { continuation ->
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                Log.d("Location", "Latitude: ${location.latitude}, Longitude: ${location.longitude}")

                try {
                    // Use Geocoder to get the place name
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    if (!addresses.isNullOrEmpty()) {
                        val address = addresses[0]
                        // Return the locality (city), or fallback to the full address
                        val locationName = address.locality ?: address.getAddressLine(0)
                        continuation.resume(locationName)
                    } else {
                        Log.d("Location", "No address found for the location")
                        continuation.resume("Unknown Location")
                    }
                } catch (e: Exception) {
                    Log.e("Location", "Geocoder error: ${e.message}")
                    continuation.resume("Error resolving location")
                }
            } else {
                Log.d("Location", "Location not found")
                continuation.resume("Unknown Location")
            }
        }.addOnFailureListener {
            Log.d("Location", "Failed to get location: ${it.message}")
            continuation.resume("Failed to get location")
        }
    }
}
@Composable
fun LocationInfo() {
    // Récupérer la largeur de l'écran
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    // Ajout d'un état pour l'adresse dynamique
    var currentLocation by remember { mutableStateOf("Bab Ezzouar Rue 02") }
    var locationName by remember { mutableStateOf("Loading...") }
    val context = LocalContext.current

// Launch a coroutine to get the current location name
    LaunchedEffect(Unit) {
        locationName = getCurrentLocationName(context = context) ?: "Unknown Location"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .width(screenWidth * 0.85f) // responsive
                .height(80.dp)
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icône de localisation
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Color(0xFFFF640D).copy(alpha = 0.05f),
                        shape = RoundedCornerShape(8.dp)
                    ), // Couleur avec opacité
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "Location Icon",
                    tint = Color(0xFFFF640D) // Couleur de l'icône
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Texte à droite
            Column(modifier = Modifier.weight(1f)) { // Utilisation de weight pour occuper l'espace restant
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start // Aligner les éléments à gauche
                ) {
                    Text(
                        text = "Current Location",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Dropdown Arrow",
                        tint = Color.Gray,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(1.dp))

                Text(
                    text = locationName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}