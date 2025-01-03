package com.example.fooddelivery.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.fooddelivery.R
import com.example.fooddelivery.ui.theme.Black1F
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.coroutines.resume

// Data Class for OpenStreetMap suggestions
data class OpenStreetMapSuggestion(
    val display_name: String,
    val lat: String,
    val lon: String
)

// Retrofit API Interface
interface OpenStreetMapApi {
    @GET("search")
    suspend fun getSuggestions(
        @Query("q") query: String,
        @Query("format") format: String = "json"
    ): List<OpenStreetMapSuggestion>
}

// Service object for Retrofit
object OpenStreetMapService {
    val api: OpenStreetMapApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenStreetMapApi::class.java)
    }
}

// Function to get the current location
suspend fun getCurrentLocation(context: Context): Pair<Double, Double>? {
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
                continuation.resume(Pair(location.latitude, location.longitude))
            } else {
                Log.d("Location", "Location not found")
                continuation.resume(null)
            }
        }.addOnFailureListener {
            Log.d("Location", "Failed to get location: ${it.message}")
            continuation.resume(null)
        }
    }
}

// Address Screen Composable
@SuppressLint("MissingPermission")
@Composable
fun AddressScreen(context: Context, navController: NavController) {

    var query by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf<List<OpenStreetMapSuggestion>>(emptyList()) }
    var selectedSuggestion by remember { mutableStateOf<OpenStreetMapSuggestion?>(null) }
    var currentLocation by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var showLocationDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF8F8F8)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Sharp.KeyboardArrowLeft,
                    contentDescription = "Call",
                    tint = Black1F,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Address",
                color = Black1F,
                fontSize = 16.sp,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Input field
        Text("Enter your address", fontSize = 14.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF8F8F8)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(R.drawable.location),
                contentDescription = "location",
                modifier = Modifier
                    .size(28.dp)
                    .padding(4.dp)
            )

            BasicTextField(
                value = query,
                onValueChange = { newQuery ->
                    query = newQuery
                    coroutineScope.launch {
                        if (query.isNotEmpty()) {
                            suggestions = OpenStreetMapService.api.getSuggestions(query)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(50.dp)
                    .background(Color(0xFFF8F8F8)),
                decorationBox = { innerTextField ->
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (query.isEmpty()) Text("Address", color = Color(0xFFF8F8F8), fontSize = 14.sp)
                        innerTextField()
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        suggestions.forEach { suggestion ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (selectedSuggestion == suggestion) Color(0xFFFFF0E7) else Color.Transparent
                    )
                    .clickable {
                        query = suggestion.display_name
                        selectedSuggestion = suggestion
                    }
                    .padding(vertical = 8.dp, horizontal = 4.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.location),
                    contentDescription = "location",
                    colorFilter = ColorFilter.tint(Color(0xFFFF640D)),
                    modifier = Modifier
                        .size(28.dp)
                        .padding(4.dp)
                )
                Text(
                    text = suggestion.display_name,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color.Gray, thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(8.dp))

        // Use current location button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    coroutineScope.launch {
                        if (!isLocationEnabled(context)) {
                            showLocationDialog = true
                        } else {
                            currentLocation = getCurrentLocation(context)
                            // After getting the location, navigate to tracking screen
                            currentLocation?.let {
                                navController.navigate("tracking_screen?lat=${it.first}&lon=${it.second}")
                            }
                        }
                    }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon/Image
            Image(
                painter = painterResource(id = R.drawable.send_2),
                contentDescription = "Location Icon",
                modifier = Modifier.size(24.dp),
            )

            Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text

            // Text
            Text(
                text = "Use my local address",
                fontSize = 14.sp,
                color = Color.Black
            )
        }

        // Display current location for testing
        currentLocation?.let {
            Text("Current location: ${it.first}, ${it.second}", fontSize = 14.sp, color = Color.Gray)
        }

        Spacer(modifier = Modifier.weight(1f))

        // If selectedSuggestion is not null, show the Add and Cancel buttons
        if (selectedSuggestion != null) {
            Column(
                Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    navController.navigate("tracking_screen?lat=${selectedSuggestion!!.lat}&lon=${selectedSuggestion!!.lon}")
                },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Black1F),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "Add",
                        color = Color.White,
                        fontSize = 16.sp,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    selectedSuggestion = null
                    query = ""
                },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 8.dp)
                        .border(1.dp, Black1F, RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = "Cancel",
                        color = Black1F,
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }

    // Location Services Dialog
    if (showLocationDialog) {
        AlertDialog(
            onDismissRequest = { showLocationDialog = false },
            title = { Text("Location Services") },
            containerColor = Color(0xFFFCF4EE),

            text = { Text("Please enable location services in your device settings.") },
            confirmButton = {
                Button(
                    onClick = {
                        showLocationDialog = false
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        context.startActivity(intent)
                    } ,
                    colors = ButtonDefaults.buttonColors(containerColor = Black1F),

                ) {
                    Text(
                        text="Enable",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            },
            dismissButton = {
                Button(onClick = { showLocationDialog = false },
                        colors = ButtonDefaults.buttonColors(Color.White),


                ){
                    Text(
                        text = "Cancel",
                        color = Black1F,
                        fontSize = 16.sp,
                    )
                }
            }
        )
    }
}

// Function to check if location services are enabled
fun isLocationEnabled(context: Context): Boolean {
    val locationMode = Settings.Secure.getInt(
        context.contentResolver, Settings.Secure.LOCATION_MODE
    )
    return locationMode != Settings.Secure.LOCATION_MODE_OFF
}
