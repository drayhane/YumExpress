package com.example.fooddelivery.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.fooddelivery.data.model.Restaurant
import com.example.fooddelivery.domain.respository.FavorisRepository
import com.example.fooddelivery.domain.respository.FavorisRepositoryImpl
import com.example.fooddelivery.domain.respository.restoRepository
import com.example.fooddelivery.domain.respository.restoRepositoryImpl
import com.example.fooddelivery.domain.usecase.GetRestoUsecase
import com.google.gson.Gson
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch
import supabaseClient

@Composable
fun RestaurantList(
    repository: restoRepository = remember { restoRepositoryImpl() },
    getrestoUseCase: GetRestoUsecase,
    searchText: String,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit,
    navController: NavHostController
) {
    //Etat pour la liste des restaurants
    val restaurants = remember { mutableStateListOf<Restaurant>() }
    // Indicateur de chargement
    val isLoading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf<String?>(null) }
    // Etat pour gérer le défilement de la liste
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    //pour mettre les restau fav
    val favoriteRestaurants = remember { mutableStateListOf<Restaurant>() }
    val favorisRes:FavorisRepository=FavorisRepositoryImpl()

    val currentUser = supabaseClient.auth.currentUserOrNull()
    val userId = currentUser?.id ?: throw Exception("User not authenticated")
    LaunchedEffect(Unit){
        val favoris = favorisRes.getFavRestaurants(userId) // Retourne une liste d'objets Restaurant
        favoriteRestaurants.clear()
        if (favoris != null) {
            favoriteRestaurants.addAll(favoris)
        }
    }



    LaunchedEffect(searchText, selectedCategory) {
        try {

            isLoading.value = true
            val fetchedRestaurants = repository.getResto()
            restaurants.clear()
            restaurants.addAll(
                fetchedRestaurants.filter {
                    (it.name.contains(searchText, ignoreCase = true)) &&// filtrer selon le nom de la catégorie et searchtext
                            (selectedCategory == null || it.category == selectedCategory)
                }
            )
        } catch (e: Exception) {
            errorMessage.value = "Failed to load restaurants: ${e.message}"
        } finally {
            isLoading.value = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Most popular",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.clickable {
                    onCategorySelected(null) // Réinitialise la sélection
                }
            )
            // Flèche pour défiler
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = "Right list icon",
                    tint = Color.Gray,
                    modifier = Modifier
                        .clickable {
                            // Défilement vers le prochain élément lorsque la flèche est cliquée
                            coroutineScope.launch {
                                val currentIndex = listState.firstVisibleItemIndex
                                val nextIndex = (currentIndex + 1).coerceAtMost(restaurants.size - 1)
                                listState.animateScrollToItem(nextIndex)
                            }
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (restaurants.isEmpty()) {
            Text(
                text = "No restaurants found.",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(restaurants) { restaurant ->
                    RestaurantCard(restaurant = restaurant , navController = navController,favorisRes=favorisRes,favorisList=favoriteRestaurants )

                }
            }
        }
    }
}

@Composable
fun RestaurantCard(restaurant: Restaurant, navController: NavHostController,favorisRes:FavorisRepository, favorisList: List<Restaurant>) {
    val coroutineScope = rememberCoroutineScope()

    // État pour suivre si le restaurant est favori ou non
    var isFavorite by remember {
        mutableStateOf(favorisList.any { it.id_restaurant == restaurant.id_restaurant })
    }

    Column(
        modifier = Modifier
            .width(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                val idresrt= Gson().toJson(restaurant.id_restaurant)
                navController.navigate("RestaurantScreen/${idresrt}") } // Handle the card click
    ) {
        // Image du restaurant
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .clip(RoundedCornerShape(12.dp))
        ) {

            Image(
                painter = rememberAsyncImagePainter(
                    model = restaurant.logo
                ),
                contentDescription = restaurant.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()

            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Nom du restaurant et icône de favoris
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = restaurant.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite icon",
                tint = if (isFavorite) Color(0xFFFF640D) else Color.Gray,
                modifier = Modifier
                    .clickable {
                        val currentUser = supabaseClient.auth.currentUserOrNull()
                        val userId = currentUser?.id ?: throw Exception("User not authenticated")
                        coroutineScope.launch {
                            try {
                                // Perform add or remove operation based on the current state
                                if (isFavorite) {
                                    println("Removing from favorites")
                                    // Call remove function
                                    favorisRes.removeFromFav(userId, restaurant.id_restaurant)

                                    // Only update state after successful removal
                                    isFavorite = false // Update state after success
                                } else {
                                    println("Adding to favorites")
                                    // Call add function
                                    favorisRes.addfavorits(userId, restaurant.id_restaurant)

                                    // Only update state after successful addition
                                    isFavorite = true // Update state after success
                                }
                            } catch (e: Exception) {
                                e.printStackTrace() // Log error for debugging
                                // Optionally, you can revert the state in case of error to avoid UI inconsistency
                                isFavorite = !isFavorite // Revert state in case of error
                            }
                        }
                    }
            )

        }





        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Delivery food: ${restaurant.delivery_price} DA",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Text(
            text = "${restaurant.delivery_time}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF640D)
        )
    }
}