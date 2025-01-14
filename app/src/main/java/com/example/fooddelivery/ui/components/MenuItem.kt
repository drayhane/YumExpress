package com.example.fooddelivery.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.fooddelivery.R
import com.google.gson.Gson

@Composable
fun MenuItem(navController: NavController, imageRes: String?, name: String, description: String, price: String, itemId:String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
        .clickable {
        val iditem= Gson().toJson(itemId)
        navController.navigate("meal/${iditem}") }, // Handle the card click
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberImagePainter(imageRes),
            contentDescription = "Menu Item",
            modifier = Modifier
                .size(124.dp, 102.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(
                description,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Row {
                Text(
                    text = price,
                    color = Color(0xFFFF6600),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(150.dp))
                Image(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = "arrow left Icon",
                )
            }

        }
    }
}

@Composable
fun MenuItemWithDivider(navController: NavController,imageRes: String?, name: String, description: String, price: String, itemId:String) {
    Column {
        MenuItem(imageRes = imageRes, name = name, description = description, price = price, navController = navController, itemId = itemId)
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Divider(
                color = Color(0xFFEBEBEB),
                modifier = Modifier
                    .width(350.dp)
                    .height(1.dp)
            )
        }
    }
}