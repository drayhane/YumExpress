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

@Composable
fun DisplayFavorits(navcontrolle:NavHostController) { Column(
modifier = Modifier
.fillMaxSize()
.padding(16.dp)
) {
    // Back Arrow and Title
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier.size(24.dp).clickable { /* Handle back navigation */ }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Favorites",
            style = MaterialTheme.typography.titleMedium
        )
    }

    // List of Favorite Items
    LazyColumn {
        items(favoriteRestaurants) { restaurant ->
            FavoriteItem(restaurant)
        }
    }
}
}
@Composable
fun FavoriteItem(restaurant: Restaurant) {
    // State for managing the heart icon's color
    var isFavorite by remember { mutableStateOf(true) }

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
                painter = painterResource(id = restaurant.imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
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
                    horizontalArrangement = Arrangement.SpaceBetween // Ajoute l'espacement
                ) {
                    // Nom et détails du restaurant à gauche
                    Column {
                        Text(
                            text = restaurant.name,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Delivery Fee: ${restaurant.deliveryFee} DZD",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Text(
                            text = restaurant.deliveryTime,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }

                    // Rating et cœur à droite
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else Color.Gray,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    isFavorite = !isFavorite
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

// Sample Data Class
data class Restaurant(
    val name: String,
    val imageRes: Int,
    val deliveryFee: Int,
    val deliveryTime: String,
    val rating: Double
)

// Sample Data
val favoriteRestaurants = listOf(
    Restaurant("Casbah Istanbul", R.drawable.casbah_image, 215, "20-30 min", 4.8),
    Restaurant("Sultan Burger", R.drawable.patisserie_image, 250, "10-20 min", 4.8),
    Restaurant("ALOHA", R.drawable.aloha_image, 215, "20-30 min", 4.8) ,
    Restaurant("Casbah Istanbul", R.drawable.casbah_image, 215, "20-30 min", 4.8),
    Restaurant("Sultan Burger", R.drawable.patisserie_image, 250, "10-20 min", 4.8),
    Restaurant("ALOHA", R.drawable.aloha_image, 215, "20-30 min", 4.8)
)
