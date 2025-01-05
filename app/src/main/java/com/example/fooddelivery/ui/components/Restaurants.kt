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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fooddelivery.data.model.Restaurant
import com.example.fooddelivery.domain.respository.restoRepository
import com.example.fooddelivery.domain.respository.restoRepositoryImpl
import com.example.fooddelivery.domain.usecase.GetRestoUsecase
import kotlinx.coroutines.launch
import coil.compose.rememberAsyncImagePainter

//---------------------------Liste des Restaurants--------------------------//
/*@Composable
fun RestaurantList(
    repository: restoRepository = remember { restoRepositoryImpl() },
    getrestoUseCase: GetRestoUsecase,
) {
    // Utiliser LocalContext.current pour obtenir le contexte
    val context = LocalContext.current

    // Variable pour stocker les restaurants récupérés
    val restaurants = remember { mutableStateListOf<Restaurant>() }

    // État pour afficher un chargement ou des erreurs
    val isLoading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    // État pour gérer le défilement de la liste
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()


    // Charger les données
    LaunchedEffect(Unit) {
        try {
            isLoading.value = true
            val fetchedRestaurants = repository.getResto()
            restaurants.clear()
            restaurants.addAll(fetchedRestaurants)
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
        // Affichage du titre et du bouton de défilement
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Most popular",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
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

        // Affichage des données ou du chargement
        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (errorMessage.value != null) {
            Text(
                text = errorMessage.value ?: "",
                color = Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            // Liste horizontale des restaurants
            LazyRow(
                state = listState,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(restaurants) { restaurant ->
                    RestaurantCard(restaurant)
                }
            }
        }
    }
}

@Composable
fun RestaurantCard(restaurant: Restaurant) {
    // État pour suivre si le restaurant est favori ou non
    var isFavorite by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .width(200.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        // Image du restaurant
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .clip(RoundedCornerShape(12.dp))
        ) {

            Image (
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
                        isFavorite = !isFavorite // Changement d'état
                    }
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Delivery food: ${restaurant.delivery_price}",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Text(
            text = restaurant.delivery_time,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF640D)
        )
    }
}
*/
@Composable
fun RestaurantList(
    repository: restoRepository = remember { restoRepositoryImpl() },
    getrestoUseCase: GetRestoUsecase,
    searchText: String // to filter restaurants
) {
    // pour obtenir le contexte
    val context = LocalContext.current

    //Etatpour stocker les restaurants récupérés
    val restaurants = remember { mutableStateListOf<Restaurant>() }

    // Etat pour les restaurants filtrés
    val filteredRestaurants = remember { mutableStateListOf<Restaurant>() }

    // Etat pour afficher un chargement ou des erreurs
    val isLoading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

   // Etat pour gérer le défilement de la liste
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()



   // Filtrer les restaurants en fonction du texte de recherche

    LaunchedEffect(searchText) { // charger when searchText changes
        try {
            isLoading.value = true
            val fetchedRestaurants = repository.getResto()
            restaurants.clear()
            restaurants.addAll(fetchedRestaurants.filter {
                it.name.contains(searchText, ignoreCase = true) // Filter by restaurant name
            })
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
        // Affichage du titre et du bouton de défilement
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Most popular",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
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
        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (errorMessage.value != null) {
            Text(
                text = errorMessage.value ?: "",
                color = Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyRow(
                state = listState,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(restaurants) { restaurant ->
                    RestaurantCard(restaurant)
                }
            }
        }
    }
}
@Composable
fun RestaurantCard(restaurant: Restaurant) {

    // État pour suivre si le restaurant est favori ou non
    var isFavorite by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .width(200.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        // Image du restaurant
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .clip(RoundedCornerShape(12.dp))
        ) {

            Image (
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
                        isFavorite = !isFavorite // Changement d'état
                    }
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Delivery food: ${restaurant.delivery_price}",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Text(
            text = restaurant.delivery_time,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF640D)
        )
    }
}