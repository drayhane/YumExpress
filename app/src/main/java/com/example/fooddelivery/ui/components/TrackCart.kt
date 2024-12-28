package com.example.fooddelivery.ui.components

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.fooddelivery.R
import com.example.fooddelivery.ui.theme.Black1F
import kotlinx.coroutines.delay

@Composable
fun TrackCard(
    deliveryTime: String,
    deliveryManName: String,
    deliveryManPhone: String,
    deliveryManImageUrl: String,
    deliveryPlace: String,
    context: Context,
    modifier: Modifier = Modifier

) {
    var activeIconIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (activeIconIndex < 7) {
            delay(150_000)  // Delay for 2.5 minutes before incrementing
            activeIconIndex++

            // Send notification based on the active index
            if (activeIconIndex == 1) {
                sendNotification(context, "Your order is taken!")
            } else if (activeIconIndex == 3) {
                sendNotification(context, "We are preparing the order.")
            } else if (activeIconIndex == 5) {
                sendNotification(context, "Order is on the route.")
            } else if (activeIconIndex == 7) {
                sendNotification(context, "Order delivered, please evaluate!")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape (topStart = 32.dp, topEnd = 32.dp))
            .background(Black1F)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        // Estimated Delivery Time Section
        Text(
            text = "Estimated Delivery time is $deliveryTime",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Your order is accepted!",
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(24.dp))

        Divider(color = Color.Gray, thickness = 0.5.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            val icons = listOf(
                R.drawable.note,
                R.drawable.car,
                R.drawable.timer,
                R.drawable.car,
                R.drawable.driving,
                R.drawable.car,
                R.drawable.tick_circle,
            )
            icons.forEachIndexed { index, icon ->
                Image(
                    painter = painterResource(icon),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        if ((index <= activeIconIndex) ) Color(0xFFFF640D) else Color(0xFFFFFFFF) // Orange for active, white for inactive
                    ),
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Divider(color = Color.Gray,  thickness = 0.5.dp)

        Spacer(modifier = Modifier.height(24.dp))

        // Delivery Man Contact Section
        Text(
            text = "Delivery man contact",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                    .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.man),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = deliveryManName,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        Text(
                            text = deliveryManPhone,
                            color = Color(0xFF8C94A7),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            IconButton(onClick = { /* Handle call action */ }) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call",
                        tint = Black1F,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

        }

        Spacer(modifier = Modifier.height(18.dp))

        // Delivery Place
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.location),
                contentDescription ="location",
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = deliveryPlace,
                color =Color(0xFF8C94A7),
                fontSize = 14.sp
            )
        }
    }
}

fun sendNotification(context: Context, message: String) {
    // For Android 13 and higher, check if we have permission to post notifications
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

            // If permission is not granted, request it
            Toast.makeText(context, "Notification permission is required", Toast.LENGTH_SHORT).show()
            return
        }
    }

    val channelId = "food_delivery_channel"
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, "Food Delivery Notifications", NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = "Notifications for food delivery status"
        }
        notificationManager.createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(context, channelId)
        .setContentTitle("Food Delivery Update")
        .setContentText(message)
        .setSmallIcon(android.R.drawable.ic_menu_info_details)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return
    }
    NotificationManagerCompat.from(context).notify(0, notification)
}
