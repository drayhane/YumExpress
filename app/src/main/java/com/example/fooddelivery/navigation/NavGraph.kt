package  com.example.fooddelivery.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fooddelivery.ui.screens.*

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Destination.Home.route,
        modifier = modifier
    ) {
        composable(Destination.Home.route) { HomeScreen() }
        composable(Destination.Orders.route) { OrdersScreen() }
        composable(Destination.Carts.route) { CartsScreen() }
        composable(Destination.Favorites.route) { FavoritesScreen() }
        composable(Destination.Profile.route) { ProfileScreen() }
    }
}
