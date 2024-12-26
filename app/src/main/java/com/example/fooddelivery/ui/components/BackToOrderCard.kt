package com.example.fooddelivery.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fooddelivery.ui.theme.Black1F
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.navigation.NavController

@Composable
fun BackToOrderCard(navController: NavController,    modifier: Modifier = Modifier
) {
    Button(
        onClick = { navController.navigateUp() },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Black1F), // Button background color
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            // Back arrow icon
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back arrow",
                tint = Color.White
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Back to order",
                color = Color.White,
                fontSize = 16.sp,
            )
        }
    }
}
