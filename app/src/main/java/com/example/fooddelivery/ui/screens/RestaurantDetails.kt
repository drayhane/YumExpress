package com.example.fooddelivery.ui.screens

import GetRestaurantUseCase
import MenuRepository
import MenuRepositoryImpl
import RestaurantRepository
import RestaurantRepositoryImpl
import android.widget.Space
import androidx.compose.foundation.Image
import com.example.fooddelivery.ui.components.CompletionDialog
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.fooddelivery.data.model.Item
import com.example.fooddelivery.data.model.Restaurant
import com.example.fooddelivery.data.model.Review
import com.example.fooddelivery.domain.usecase.GetReviewUseCase

import com.example.fooddelivery.ui.components.MenuItem
import com.example.fooddelivery.ui.components.TabItem
import com.example.fooddelivery.ui.components.MenuItemWithDivider

import com.example.fooddelivery.ui.components.CardItem
import com.example.fooddelivery.ui.components.FeedbackDialog
@Composable
fun RestaurantDetailsScreen(
    restaurantId: String,
    getReviewUseCase: GetReviewUseCase
) {
    var reviews by remember { mutableStateOf<List<Pair<Review, String>>?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(restaurantId) {
        try {
            reviews = getReviewUseCase(restaurantId)
        } catch (e: Exception) {
            // Handle error (e.g., show a message)
            reviews = emptyList()
        }
        isLoading = false
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Top Image
        item {
            Image(
                painter = painterResource(id = R.drawable.burger_image),
                contentDescription = "Top Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }

        // Overlapping Item
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-15).dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White),
                        contentAlignment = Alignment.Center

            ) {
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
                    Spacer(modifier = Modifier.height(8.dp))

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
        }

        // Remaining Content
        item {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp,2.dp))
            } else {
                if (!reviews.isNullOrEmpty()) {
                    Column(modifier = Modifier.padding(16.dp,2.dp)) {
                        reviews?.forEach { (review, userName) ->
                            CardItem(
                                name = userName,
                                date = review.date ?: "Unknown Date",
                                comment = review.review ?: "No Comment",
                                rating = review.note ?: 0
                            )
                        }
                    }
                } else {
                    Text(
                        text = "No reviews available for this restaurant.",
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }

}