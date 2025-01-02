package com.example.fooddelivery


import DisplayFavorits
import DisplayOrders
import Displaydetail
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fooddelivery.data.local.OrderEntity
import com.example.fooddelivery.domain.respository.UserRepository
import com.example.fooddelivery.domain.respository.UserRepositoryImpl
import com.example.fooddelivery.ui.screens.DisplayEdit
import com.example.fooddelivery.ui.screens.DisplayPanier
import com.example.fooddelivery.ui.screens.DisplayProfil
import com.example.fooddelivery.ui.screens.Displaymeal
import com.example.fooddelivery.ui.theme.FoodDeliveryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up the theme and UI content of the app
        setContent {
            FoodDeliveryTheme{
                val navController = rememberNavController()
                main(navController)

            }
        }
    }
}
@Composable
fun  main(navcontroller : NavHostController)
{
    val sampleOrders = listOf(
    OrderEntity(
        id = 1,
        restaurantName = "Casbah Istanbul",
        totalPrice = "1495 DA | 2 products",
        date = "12/02/24 17:43",
        status = "Delivered",
        imageRes = R.drawable.casbah_image // Remplace par ton image
    ),
    OrderEntity(
        id = 2,
        restaurantName = "ALOHA",
        totalPrice = "5000 DA | 1 product",
        date = "12/02/24 17:43",
        status = "Canceled",
        imageRes = R.drawable.aloha_image // Remplace par ton image
    ),
    OrderEntity(
        id = 3,
        restaurantName = "Patisserie",
        totalPrice = "700 DA | 3 products",
        date = "12/02/24 17:43",
        status = "Delivered",
        imageRes = R.drawable.patisserie_image // Remplace par ton image
    )

)

    val userId = "1"
    val coroutineScope = rememberCoroutineScope()
    val userRepository: UserRepository = UserRepositoryImpl()
val itemid="2"

    var startDestination = "meal"
    NavHost(navController = navcontroller, startDestination ){
        composable ("Profil")   {

                DisplayProfil(navcontroller, userId)

        }
        composable("EditProfil"){DisplayEdit(navcontroller,userId)}
        composable("Orders")    {DisplayOrders(navcontroller,orders = sampleOrders)}
        composable("details")   {Displaydetail(navcontroller)}
        composable("favorits")  {DisplayFavorits(navcontroller)}
        composable("meal")      {Displaymeal(navcontroller,itemid,userId)}
        composable("panier")    {DisplayPanier(navcontroller)}
        }
    }


