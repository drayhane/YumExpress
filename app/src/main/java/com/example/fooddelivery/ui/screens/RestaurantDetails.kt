package com.example.fooddelivery.ui.screens

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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

@Composable
fun RestaurantDetailsScreen() {
    Column(modifier = Modifier.fillMaxSize()
        .background(color = Color(0xFFFFFFFF))) {
        // Top Image Section
        Box {
            Image(
                painter = painterResource(id = R.drawable.burger_image), // Replace with your image resource
                contentDescription = "Restaurant Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.Top, // Alignement vertical global
                horizontalArrangement = Arrangement.SpaceBetween // Espace entre les icônes
            ) {
                // Icone de retour (alignée à gauche)
                IconButton(
                    onClick = { /* Action pour l'icône retour */ },
                    modifier = Modifier.padding(8.dp) // Ajuster les marges
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow_left),
                        contentDescription = "Arrow Left Icon"
                    )
                }

                // Icone coeur (alignée à droite)
                IconButton(
                    onClick = { /* Action pour l'icône favori */ },
                    modifier = Modifier.padding(8.dp) // Ajuster les marges
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.heart),
                        contentDescription = "Heart Icon"
                    )
                }
            }

        }

        // Restaurant Info
        Column(modifier = Modifier.padding(16.dp)) {
            Text("House of Burgers", color = Color(0xFF1F1F1F), fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "arrow left Icon",
                )
                Text(
                    text = "  4.8 (1.5k) • 880 reviews ",
                    fontSize = 14.sp,
                    color = Color(0xFF1F1F1F)
                )
                Text(
                    text = " (see all reviews) ",
                    fontSize = 14.sp,
                    color = Color(0xFFFF6600), // Orange color
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.call),
                    contentDescription = "arrow left Icon",
                )

                Text("  Phone number: 0676180978", fontSize = 14.sp, color = Color(0xFF1F1F1F))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF1F1F1F), RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFFFFF), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.group),
                            contentDescription = "arrow left Icon",
                        )
                        Text("  250 DZD", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    Text("Delivery Fee", fontSize = 12.sp, color = Color.Gray)
                }
                Divider(
                    color = Color(0xFF1F1F1F), modifier = Modifier
                        .width(1.dp)
                        .height(50.dp)
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.clock),
                            contentDescription = "arrow left Icon",
                        )
                        Text("  10-20 min", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    Text("Delivery Time", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        // Tabs
        val selectedTabIndex = 0

        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp) // Taille totale de la TabRow
                .background(Color.Transparent),
            edgePadding = 24.dp,
            indicator = {}
        ) {
            // Onglet Burgers (Sélectionné)
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { /* Handle click */ },
                modifier = Modifier
                    .background(
                        color = Color(0xFFFF640D), // Couleur de fond sélectionnée
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Burgers",
                    color = Color.White, // Texte blanc
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Onglet Pizza (Non sélectionné)
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { /* Handle click */ },
                modifier = Modifier
                    .background(
                        color = Color(0xFFFFF9F5), // Fond gris clair
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Pizza",
                    color = Color(0xFFFFCFB4), // Texte orange pâle
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            // Onglet Sandwiches (Non sélectionné)
            Tab(
                selected = selectedTabIndex == 2,
                onClick = { /* Handle click */ },
                modifier = Modifier
                    .background(
                        color = Color(0xFFFFF9F5), // Fond gris clair
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Sandwiches",
                    color = Color(0xFFFFCFB4), // Texte orange pâle
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            // Onglet Tacos (Non sélectionné)
            Tab(
                selected = selectedTabIndex == 3,
                onClick = { /* Handle click */ },
                modifier = Modifier
                    .background(
                        color = Color(0xFFFFF9F5), // Fond gris clair
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Tacos",
                    color = Color(0xFFFFCFB4), // Texte orange pâle
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        // Menu List
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                MenuItem("New York", "450 DA")
                Divider(
                    color = Color.LightGray, modifier = Modifier
                        .width(100.dp)
                        .height(1.dp)
                )
                MenuItem("Dallas", "500 DA")
                Divider(
                    color = Color.LightGray, modifier = Modifier
                        .width(100.dp)
                        .height(1.dp)
                )
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
            Image(
                painter = painterResource(id = R.drawable.new_york),
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
                    "100gr de steak haché, saucisse pure Bœuf, salade, tomate, fromage, sauce BBQ",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = price,
                    color = Color(0xFFFF6600),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

    }
}