package com.example.fooddelivery.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fooddelivery.ui.theme.Black1F


@Composable
fun DeliverySuccessScreen(navController: NavController,id_res :String) {
  Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

      Image(
          painter = painterResource(id =com.example.fooddelivery.R.drawable.yumexpress),
          contentDescription = "Scooter delivery",
          modifier = Modifier.size(200.dp)
      )

        Spacer(modifier = Modifier.height(16.dp))


        Image(
            painter = painterResource(id =com.example.fooddelivery.R.drawable.deliverymotorcycle),
            contentDescription = "Scooter delivery",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))


        Text(
            text = "Your order has been delivered successfully!",
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Explore a world of flavors at your fingertips.\nCraving something delicious? We've got it covered!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))
       Row(
            modifier = Modifier
                .clickable {
                    navController.navigate("RestaurantScreen/${id_res}")
                },
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text(
                text = "Leave review",
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = TextDecoration.Underline // Add underline here
                ),
                color = Color(0xFFFF640D)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Arrow",
                tint = Color(0xFFFF640D)
            )
        }

        Spacer(modifier = Modifier.weight(0.5f))

        // Exit Button
        Button(
            onClick = {navController.navigate("HomeScreen")
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Black1F),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = "Exit",
                color = Color.White,
                fontSize = 16.sp,
            )
        }
    }
}
