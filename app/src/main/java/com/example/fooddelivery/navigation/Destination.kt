package  com.example.fooddelivery.navigation



import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector)
{
    object Home : Destination("home", "Home", Icons.Filled.Home)
    object Orders : Destination("orders", "Orders", Icons.Filled.Assignment)
    object Carts : Destination("carts", "Carts", Icons.Filled.ShoppingCart)
    object Favorites : Destination("favorites", "Favorites", Icons.Filled.Favorite)
    object Profile : Destination("profile", "Profile", Icons.Filled.Person)
}
