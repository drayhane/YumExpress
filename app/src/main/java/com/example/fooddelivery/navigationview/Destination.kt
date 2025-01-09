package  com.example.fooddelivery.navigationview



import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector)
{
    object Home : Destination("HomeScreen", "Home", Icons.Filled.Home)
    object Orders : Destination("Orders", "Orders", Icons.Filled.Assignment)
    object Carts : Destination("panier", "Carts", Icons.Filled.ShoppingCart)
    object Favorites : Destination("favorits", "Favorites", Icons.Filled.Favorite)
    object Profile : Destination("Profil", "Profile", Icons.Filled.Person)
}
