package  com.example.fooddelivery.navigationview

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Destination.Home,
        Destination.Orders,
        Destination.Carts,
        Destination.Favorites,
        Destination.Profile
    )

    NavigationBar(
        modifier = Modifier

    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { destination ->
            NavigationBarItem(
                label = {
                    Text(
                        destination.label,
                        color = if (currentRoute == destination.route) Color(0xFFFF640D) else MaterialTheme.colorScheme.onBackground
                    )
                },
                icon = {
                        Icon(
                            destination.icon,
                            contentDescription = destination.label,
                            tint = if (currentRoute == destination.route) Color(0xFFFF640D) else MaterialTheme.colorScheme.onBackground
                        )
                },
                selected = currentRoute == destination.route,
                onClick = {
                    navController.navigate(destination.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFFF640D), // Couleur de l'icône sélectionnée
                    selectedTextColor = Color(0xFFFF640D), // Couleur du texte sélectionné
                    unselectedIconColor = Color(0xFF5E5F60) , // Couleur par défaut pour l'icône
                    unselectedTextColor =Color(0xFF5E5F60) // Couleur par défaut pour le texte
                )
            )
        }
    }
}

