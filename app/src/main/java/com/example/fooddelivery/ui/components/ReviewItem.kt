package com.example.fooddelivery.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fooddelivery.R

@Composable
fun CardItem(name: String, date: String, comment: String, rating: Int) {
    Box(
        modifier = Modifier
            .width(350.dp)
            .height(140.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
    ) {
        Column{
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Profile Image
                Box(
                    modifier = Modifier
                        .size(41.dp)
                        .clip(CircleShape)
                        .background(Color.Gray) // Replace with an image resource
                ) {
                    // Placeholder for image
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = name,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    RatingBar(rating = rating)
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = date,
                    fontSize = 12.sp,
                    color = Color(0xFF8C94A7)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = comment,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                color = Color(0xFF8C94A7),
                lineHeight = 14.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Divider(
                color = Color(0xFFEBEBEB),
                modifier = Modifier
                    .width(350.dp)
                    .height(1.dp)
            )

        }
    }
}

@Composable
fun RatingBar(rating: Int) {
    Row {
        repeat(5) { index ->
            val starColor = if (index < rating) Color(0xFFFF640D) else Color.Gray
            Image(
                painter = painterResource(id = R.drawable.star), // Replace with your star icon resource
                contentDescription = "Star",
                modifier = Modifier.size(16.dp), // Adjust the size of the star as needed
                colorFilter = ColorFilter.tint(starColor) // Apply tint to change color
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

