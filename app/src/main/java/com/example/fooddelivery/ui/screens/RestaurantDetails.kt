package com.example.fooddelivery.ui.screens

import android.widget.Space
import com.example.fooddelivery.ui.components.BottomNavigationBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fooddelivery.R

import com.example.fooddelivery.ui.components.MenuItem
import com.example.fooddelivery.ui.components.TabItem
import com.example.fooddelivery.ui.components.MenuItemWithDivider

import com.example.fooddelivery.ui.components.CardItem

@Composable
fun RestaurantDetailsScreen() {
    // State to toggle between restaurant details and reviews
    val showReviews = remember{ mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Top Image Section
        item {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.burger_image),
                    contentDescription = "Top Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { /* Action for back icon */ },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.arrow_left),
                            contentDescription = "Arrow Left Icon"
                        )
                    }
                    IconButton(
                        onClick = { /* Action for favorite icon */ },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.heart),
                            contentDescription = "Heart Icon"
                        )
                    }
                }
            }
        }

        // Conditional content: Show either restaurant info or reviews
        if (!showReviews.value) {
            // Restaurant Info
            item {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "House of Burgers",
                        color = Color(0xFF1F1F1F),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.star),
                            contentDescription = "Star Icon"
                        )
                        Text(
                            text = "  4.8 (1.5k) • 880 reviews ",
                            fontSize = 14.sp,
                            color = Color(0xFF1F1F1F)
                        )
                        Text(
                            text = " (see all reviews) ",
                            fontSize = 14.sp,
                            color = Color(0xFFFF6600),
                            modifier = Modifier.clickable {
                                showReviews.value = true // Toggle to show reviews
                            }
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.call),
                            contentDescription = "Call Icon"
                        )
                        Text(
                            text = "  Phone number: 0676180978",
                            fontSize = 14.sp,
                            color = Color(0xFF1F1F1F)
                        )
                    }
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .width(340.dp)
                        .height(65.dp)
                        .border(1.dp, Color(0xFF1F1F1F), RoundedCornerShape(8.dp))
                        .background(Color(0xFFFFFFFF), RoundedCornerShape(8.dp))
                        .padding(12.dp), // Padding inside the Box
                    contentAlignment = Alignment.Center // Ensures the content (Row) is centered
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly, // Space elements evenly
                        verticalAlignment = Alignment.CenterVertically, // Align Row content vertically
                        modifier = Modifier.fillMaxWidth() // Make Row span the full width of the Box
                    ) {
                        // First Column
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.group),
                                    contentDescription = "Delivery Fee Icon"
                                )
                                Text(
                                    text = "  250 DZD",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                            Text(
                                text = "Delivery Fee",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        // Divider
                        Divider(
                            color = Color(0xFF1F1F1F),
                            modifier = Modifier
                                .width(1.dp)
                                .height(50.dp)
                        )

                        // Second Column
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.clock),
                                    contentDescription = "Delivery Time Icon"
                                )
                                Text(
                                    text = "  10-20 min",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                            Text(
                                text = "Delivery Time",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }


            }
            item {
                // Tabs
                val selectedTabIndex = 0

                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(Color.Transparent)
                        .padding(16.dp),
                    edgePadding = 8.dp,
                    indicator = {},
                    divider = {}
                ) {
                    TabItem("Burgers", selectedTabIndex == 0, true)
                    TabItem("Pizza", selectedTabIndex == 1, false)
                    TabItem("Sandwiches", selectedTabIndex == 2, false)
                    TabItem("Tacos", selectedTabIndex == 3, false)
                }
            }

            item {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp) // Set a fixed height for the independent scrollable list
                ){
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            MenuItemWithDivider(
                                imageRes = R.drawable.new_york,
                                name = "New York",
                                description = "100gr de steak haché, salade, tomate, fromage, sauce BBQ",
                                price = "450 DA"
                            )
                            MenuItemWithDivider(
                                imageRes = R.drawable.albama,
                                name = "Dallas",
                                description = "Steak, cheddar, bacon croustillant, sauce BBQ",
                                price = "500 DA"
                            )
                            MenuItem(
                                imageRes = R.drawable.miami,
                                name = "Miami",
                                description = "Poulet croustillant, avocat, salade, sauce épicée",
                                price = "500 DA"
                            )
                            MenuItem(
                                imageRes = R.drawable.miami,
                                name = "Miami",
                                description = "Poulet croustillant, avocat, salade, sauce épicée",
                                price = "500 DA"
                            )
                        }
                    }

                }


            }
        } else {
            // Reviews Section
            item {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "House of Burgers",
                        color = Color(0xFF1F1F1F),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.star),
                            contentDescription = "Star Icon"
                        )
                        Text(
                            text = "  4.8 (1.5k) • 880 reviews ",
                            fontSize = 14.sp,
                            color = Color(0xFF1F1F1F)
                        )
                    }
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp) // Set a fixed height for the independent scrollable list
                ){
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    item {
                        CardItem(
                            name = "Derbal Rayhane",
                            date = "08/11/2024",
                            comment = "The New York burger was delicious! The bun was soft, the meat juicy, and the fresh toppings and sauce were perfectly balanced – a great choice for burger lovers!",
                            rating = 3
                        )
                        CardItem(
                            name = "Derbal Rayhane",
                            date = "08/11/2024",
                            comment = "The New York burger was delicious! The bun was soft, the meat juicy, and the fresh toppings and sauce were perfectly balanced – a great choice for burger lovers!",
                            rating = 4
                        )
                        CardItem(
                            name = "Derbal Rayhane",
                            date = "08/11/2024",
                            comment = "The New York burger was delicious! The bun was soft, the meat juicy, and the fresh toppings and sauce were perfectly balanced – a great choice for burger lovers!",
                            rating = 2
                        )
                        CardItem(
                            name = "Derbal Rayhane",
                            date = "08/11/2024",
                            comment = "The New York burger was delicious! The bun was soft, the meat juicy, and the fresh toppings and sauce were perfectly balanced – a great choice for burger lovers!",
                            rating = 5
                        )
                        CardItem(
                            name = "Derbal Rayhane",
                            date = "08/11/2024",
                            comment = "The New York burger was delicious! The bun was soft, the meat juicy, and the fresh toppings and sauce were perfectly balanced – a great choice for burger lovers!",
                            rating = 1
                        )
                        CardItem(
                            name = "Derbal Rayhane",
                            date = "08/11/2024",
                            comment = "The New York burger was delicious! The bun was soft, the meat juicy, and the fresh toppings and sauce were perfectly balanced – a great choice for burger lovers!",
                            rating = 4
                        )
                    }

                }
            }
            }
            item {

                BottomNavigationBar()

            }
        }
    }
}







