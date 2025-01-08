package com.example.fooddelivery.ui.screens

import AddReviewUseCase
import MenuRepository
import MenuRepositoryImpl
import RestaurantRepository
import RestaurantRepositoryImpl
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScrollableTabRow
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.example.fooddelivery.R
import com.example.fooddelivery.data.model.Item
import com.example.fooddelivery.data.model.Restaurant
import com.example.fooddelivery.data.model.Review
import com.example.fooddelivery.domain.usecase.GetReviewUseCase
import coil.compose.rememberImagePainter

import com.example.fooddelivery.ui.components.TabItem
import com.example.fooddelivery.ui.components.MenuItemWithDivider

import com.example.fooddelivery.ui.components.CardItem
import com.example.fooddelivery.ui.components.FeedbackDialog



@Composable
fun RestaurantScreen(
    context: Context,
    repository: RestaurantRepository = remember { RestaurantRepositoryImpl( context= context ) },
    addReviewUseCase: AddReviewUseCase,
    restaurantId: String,
    getReviewUseCase: GetReviewUseCase,
) {

    val menuRepository: MenuRepository = remember { MenuRepositoryImpl() }
    var restaurant by remember { mutableStateOf<Restaurant?>(null) }
    var menuItems by remember { mutableStateOf<List<Item>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var reviews by remember { mutableStateOf<List<Pair<Review, String>>?>(null) }

    // Static restaurantId = "1"
    val restaurantId = "1"
    val userId="7ccfe700-012b-4cc3-90b0-0f1c16ef3537"

    // Fetch data when the Composable is launched
    LaunchedEffect(Unit) {
        try {
            val fetchedRestaurant = repository.getRestaurantById(restaurantId)
            val fetchedMenuItems = menuRepository.getMenuItemsByRestaurantId(restaurantId)
            restaurant = fetchedRestaurant
            menuItems = fetchedMenuItems
            isLoading = false
        } catch (e: Exception) {
            isLoading = false
            errorMessage = "Failed to load restaurant: ${e.localizedMessage}"
        }
    }

    LaunchedEffect(restaurantId) {
        try {
            reviews = getReviewUseCase(restaurantId)
        } catch (e: Exception) {
            // Handle error (e.g., show a message)
            reviews = emptyList()
        }
        isLoading = false
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else if (errorMessage != null) {
        Text(text = errorMessage!!, color = Color.Red)
    } else {
        restaurant?.let { fetchedRestaurant ->
            val types = menuItems.map { it.Type }.distinct().sorted()
            val tabTitles = listOf("All") + types // Add "All" at the beginning

            var selectedTab by remember { mutableStateOf(0) }

            // Filtered menu items based on selected tab
            val filteredMenuItems = when (selectedTab) {
                0 -> menuItems // "All" case
                else -> menuItems.filter { it.Type == tabTitles[selectedTab] }
            }

            // State to toggle between restaurant details and reviews
            val showReviews = remember{ mutableStateOf(false) }
            val showDialog = remember { mutableStateOf(false) }
            val showCompletionDialog = remember { mutableStateOf(false) }
            val restaurantImage = fetchedRestaurant.logo


            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Top Image Section
                item {
                    Box {
                        Image(
                            painter = rememberImagePainter(restaurantImage),
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
                                    text = "${fetchedRestaurant.name}",
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
                                        text = "  ${fetchedRestaurant.rating} • ${fetchedRestaurant.nbr_reviews} reviews ",
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
                                        text = "  Phone number: ${fetchedRestaurant.phone}",
                                        fontSize = 14.sp,
                                        color = Color(0xFF1F1F1F)
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(id = R.drawable.location),
                                        contentDescription = "Location Icon",
                                        modifier = Modifier.size(16.dp),
                                        colorFilter = ColorFilter.tint(Color(0xFFFF6600))

                                    )
                                    Text(
                                        text = "  Address: ${fetchedRestaurant.location}",
                                        fontSize = 14.sp,
                                        color = Color(0xFF1F1F1F)
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(id = R.drawable.facebook),
                                        contentDescription = "Facebook Icon",
                                        modifier = Modifier
                                            .size(16.dp)
                                    )
                                    Text(
                                        text = "  ${fetchedRestaurant.facebook}",
                                        fontSize = 14.sp,
                                        color = Color(0xFF1F1F1F)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Image(
                                        painter = painterResource(id = R.drawable.instagram),
                                        contentDescription = "Instagram Icon",
                                        modifier = Modifier
                                            .size(16.dp)
                                    )
                                    Text(
                                        text = "  @${fetchedRestaurant.instagramme}",
                                        fontSize = 14.sp,
                                        color = Color(0xFF1F1F1F)
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(id = R.drawable.sms),
                                        contentDescription = "Email Icon",
                                        modifier = Modifier
                                            .size(16.dp)
                                    )
                                    Text(
                                        text = "  ${fetchedRestaurant.email}",
                                        fontSize = 14.sp,
                                        color = Color(0xFF1F1F1F)
                                    )

                                }
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
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "${fetchedRestaurant.delivery_price} DA",
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
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "${fetchedRestaurant.delivery_time}",
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

                        ScrollableTabRow(
                            selectedTabIndex = selectedTab,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .background(Color.Transparent)
                                .padding(16.dp),
                            edgePadding = 8.dp,
                            indicator = {},
                            divider = {}
                        ) {
                            tabTitles.forEachIndexed { index, title ->
                                TabItem(
                                    title = title,
                                    selected = selectedTab == index,
                                    onClick = { selectedTab = index }
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
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(filteredMenuItems) { menuItem ->
                                    MenuItemWithDivider(
                                        imageRes = menuItem.image, // Replace with actual image logic
                                        name = menuItem.name,
                                        description = menuItem.ingredient,
                                        price = "${menuItem.price} DA"
                                    )
                                }
                            }

                        }


                    }
                } else {
                    // Reviews Section
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
                                text = "${fetchedRestaurant.name}",
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
                                    text = "  ${fetchedRestaurant.rating} • ${fetchedRestaurant.nbr_reviews} reviews ",
                                    fontSize = 14.sp,
                                    color = Color(0xFF1F1F1F)
                                )
                            }
                        }

                    }
                    }
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
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(94.dp)
                                .padding(14.dp)
                                .background(Color.White)
                        ){
                            Button(
                                onClick = { showDialog.value = true },
                                modifier = Modifier
                                    .width(358.dp)
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Share your feedback",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }

                        if (showDialog.value) {
                            FeedbackDialog(
                                restaurantId = restaurantId,
                                userId = userId,
                                addReviewUseCase = addReviewUseCase,
                                onDismiss = { showDialog.value = false },
                                onSubmit = {
                                    showDialog.value = false
                                    showCompletionDialog.value = true
                                }
                            )
                        }

                        // Completion Dialog
                        if (showCompletionDialog.value) {
                            CompletionDialog(onDismiss = { showCompletionDialog.value = false })
                        }

                    }
                }
            }
        }
    }
}