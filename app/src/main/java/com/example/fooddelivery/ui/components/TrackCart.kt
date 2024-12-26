package com.example.fooddelivery.ui.components

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
    modifier: Modifier = Modifier

) {
    var activeIconIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        activeIconIndex=-1;
        while (activeIconIndex < 7) {
            delay(10_000)
            activeIconIndex++
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
