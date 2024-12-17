package com.example.fooddelivery.ui.screens

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fooddelivery.R

@Composable
fun RestaurantDetailsScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        // Top Image Section
        Box {
            Image(
                painter = painterResource(id = R.drawable.burger_image), // Replace with your image resource
                contentDescription = "Restaurant Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            IconButton(
                onClick = { /* Add favorite action */ },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .background(Color.White.copy(alpha = 0.6f), shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorite Icon",
                    tint = Color.Black
                )
            }
        }

        // Restaurant Info
        Column(modifier = Modifier.padding(16.dp)) {
            Text("House of Burgers", fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "⭐ 4.8 (1.5k) • 880 reviews ",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = " (see all reviews) ",
                    fontSize = 14.sp,
                    color = Color(0xFFFF6600), // Orange color
                )
            }

            Text("📞 Phone number: 0676180978", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF6F6F6), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("250 DZD", fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Delivery Fee", fontSize = 12.sp, color = Color.Gray)
                }
                Divider(
                    color = Color.LightGray, modifier = Modifier
                        .width(1.dp)
                        .height(30.dp)
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("10-20 min", fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Delivery Time", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        // Tabs
        ScrollableTabRow(selectedTabIndex = 0) {
            Tab(selected = true, onClick = { /* Handle click */ }) {
                Text("Burgers", fontWeight = FontWeight.Bold, color = Color(0xFFFF6600))
            }
            Tab(selected = false, onClick = { /* Handle click */ }) {
                Text("Pizza", color = Color.Gray)
            }
            Tab(selected = false, onClick = { /* Handle click */ }) {
                Text("Sandwiches", color = Color.Gray)
            }
            Tab(selected = false, onClick = { /* Handle click */ }) {
                Text("Tacos", color = Color.Gray)
            }
        }

        // Menu List
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                MenuItem("New York", "450 DA")
                Divider()
                MenuItem("Dallas", "500 DA")
                Divider()
                MenuItem("Miami", "500 DA")
            }
        }
    }
}

@Composable
fun MenuItem(name: String, price: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.new_york), // Replace with your image resource
                contentDescription = "Menu Item",
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 8.dp),
                contentScale = ContentScale.Crop
            )
            Column {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(
                    "100gr de steak haché, sauce BBQ, tomate, fromage...",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
        Text(
            text = price,
            color = Color(0xFFFF6600),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}