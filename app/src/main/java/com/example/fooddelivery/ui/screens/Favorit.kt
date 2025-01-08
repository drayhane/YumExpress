import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fooddelivery.R
import androidx.navigation.NavHostController
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.lint.Names.Runtime.LaunchedEffect
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberAsyncImagePainter
import com.example.fooddelivery.data.model.Restaurant
import com.example.fooddelivery.domain.respository.FavorisRepository
import com.example.fooddelivery.domain.respository.FavorisRepositoryImpl
import com.example.fooddelivery.domain.respository.RestaurantRepository
import com.example.fooddelivery.domain.respository.RestaurantRepositoryImpl
import kotlinx.coroutines.launch

@Composable
fun DisplayFavorits(navcontrolle: NavHostController, userId: String) {
    var favoriteRestaurants by remember { mutableStateOf<List<Restaurant>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val favorisRepository: FavorisRepository = FavorisRepositoryImpl()

    LaunchedEffect(userId) {
        try {
            val restaurants = getFavoriteRestaurants(userId)
            if (restaurants != null) {
                favoriteRestaurants = restaurants
            } else {
                errorMessage = "Erreur : La liste des restaurants favoris est vide."
            }
        } catch (e: Exception) {
            errorMessage = "Erreur : ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { /* Action de retour */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ret_btn),
                    contentDescription = "Back Button",
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Favorites",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> Text(text = "Chargement...", style = MaterialTheme.typography.bodyMedium)
            errorMessage != null -> Text(text = errorMessage!!, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
            favoriteRestaurants.isEmpty() -> {
                // Affichage du cœur rose avec ombre quand la liste est vide
                HeartWithShadow()
            }
            else -> LazyColumn {
                items(favoriteRestaurants) { restaurant ->
                    FavoriteItem(
                        restaurant = restaurant,
                        userid = userId,
                        favorisRepository=favorisRepository,
                        onRemoveFavorite = { removedRestaurant ->
                            // Supprimer du favori
                            favoriteRestaurants = favoriteRestaurants.filterNot { it.id_restaurant == removedRestaurant.id_restaurant }
                        }
                    )
                }
            }
        }
    }
}
@SuppressLint("CoroutineCreationDuringComposition*")
@Composable
fun FavoriteItem(restaurant: Restaurant, userid: String, favorisRepository: FavorisRepository, onRemoveFavorite: (Restaurant) -> Unit) {
    // var isFavorite by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope() // Declare coroutine scope here

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Restaurant Image
            Image(
                painter = rememberAsyncImagePainter(model = restaurant.logo),
                contentDescription = "Item Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            // Restaurant Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = restaurant.name,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Delivery Fee: ${restaurant.delivery_price} DZD",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Text(
                            text = restaurant.delivery_time,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite",
                            tint =  Color.Red,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {

                                    // Use coroutine scope to launch the async task instead of LaunchedEffect
                                    coroutineScope.launch {
                                        favorisRepository.removeFromFav(userid, restaurant.id_restaurant)
                                        onRemoveFavorite(restaurant)
                                    }

                                }
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = restaurant.rating.toString(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun HeartWithShadow() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        // Using a column to add text below the heart
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Heart with shadow having the same shape
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Empty Favorites Heart",
                tint = Color(0xFFEC4D87), // Rose
                modifier = Modifier
                    .size(100.dp)
                    .shadow(10.dp, RoundedCornerShape(50.dp)) // Ombre
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Text below the heart with "A Favorite?" in bold and the other text normal
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "A Favorite?",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Add restaurants to your favorites to order faster from them in the future!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
